package com.dekastudio.database;


import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.dekastudio.helper.Constant;
import com.dekastudio.model.ModelCuaca;
import com.dekastudio.model.ModelLokasi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuacaDatabase {
    private RequestQueue requestQueue;
    private ListenerCuaca listenerCuaca;
    private int jumlahHari = 90;
    private DatabaseHelper databaseHelper;
    private List<ModelCuaca> listCuaca = new ArrayList<>();
    private ModelLokasi lokasi = new ModelLokasi();

    public CuacaDatabase(Context context, RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        this.databaseHelper = new DatabaseHelper(context);
    }

    public boolean apakahPerluUpdate(){
        List<ModelLokasi> listLokasi = databaseHelper.bacaSemuaData(new ModelLokasi());
        for(ModelLokasi lokasi : listLokasi){
            long saatIni = (System.currentTimeMillis() / 1000L);
            long terahirUpdate = ModelCuaca.bacaTerahirUpdateByLokasi(databaseHelper.getOpenDatabase(), lokasi);
            long lama5Hari = (5L*(24L*60L*60L));
            if((saatIni-terahirUpdate) >= lama5Hari){
                return true;
            }
        }
        return false;
    }

    public void updateCuaca(){
        List<ModelLokasi> listLokasi = databaseHelper.bacaSemuaData(new ModelLokasi());
        for(ModelLokasi lokasi : listLokasi){
            long saatIni = (System.currentTimeMillis() / 1000L);
            long terahirUpdate = ModelCuaca.bacaTerahirUpdateByLokasi(databaseHelper.getOpenDatabase(), lokasi);
            long lama5Hari = (5L*(24L*60L*60L));
            if((saatIni-terahirUpdate) >= lama5Hari){
                Location customLokasi = new Location("customLokasi");
                customLokasi.setLatitude(lokasi.getLatitude());
                customLokasi.setLongitude(lokasi.getLongitude());
                customLokasi.setAltitude(lokasi.getAltitude());
                downloadCuaca(customLokasi, lokasi.getNamaDaerah());
                //hanya ijinkan update satu lokasi utk 1 waktu, mencegah lamanya update dan hemat data user.
                return;
            }
        }
    }

    private String namaLokasi = null;
    public void downloadCuaca(Location location, String namaLokasi){
        this.namaLokasi = namaLokasi;
        lokasi.setLatitude(location.getLatitude());
        lokasi.setLongitude(location.getLongitude());
        lokasi.setAltitude(location.getAltitude());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ACCU_WEATHER_URL, response -> {
           //grabCuaca(getUrl(response), 0);
            grabCuaca(getUrl(response));
        }, error -> {
            VolleyLog.d(Constant.TAG, "Error dl cuaca: " + error.getMessage());
            if(listenerCuaca!=null){
                listenerCuaca.onGagalDownload();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("s", String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void grabCuaca(String url){
        for(int i =0; i<jumlahHari; i++) {
            final int posPlus = i+1;
            String urlFix = url + String.valueOf(posPlus);
            StringRequest stringRequest = new StringRequest(urlFix, response -> {
                //tambahkan cuaca terdownload ke list
                listCuaca.add(parseHtmlKeModel(response, posPlus));
                if(listCuaca.size()==90){
                    simpanKeDatabase();
                }
            }, error -> {
                VolleyLog.d(Constant.TAG, "Error dl cuaca : " + error.getMessage());
                if (listenerCuaca != null) {
                    listenerCuaca.onGagalDownload();
                }
            });
            requestQueue.add(stringRequest);
        }
    }

    //grab cuaca secara asyncronus satu persatu
//    private void grabCuaca(String url, int posisi){
//        String urlFix = url + String.valueOf(posisi+1);
//        StringRequest stringRequest = new StringRequest(urlFix, response -> {
//            //tambahkan cuaca terdownload ke list
//            listCuaca.add(parseHtmlKeModel(response, posisi+1));
//            if(posisi+1<90 && listCuaca.size()<90) {
//                grabCuaca(url, posisi + 1);
//            }else {
//                simpanKeDatabase();
//            }
//        }, error -> {
//            VolleyLog.d(Constant.TAG, "Error dl cuaca : " + error.getMessage());
//            if(listenerCuaca!=null){
//                listenerCuaca.onGagalDownload();
//            }
//        });
//        requestQueue.add(stringRequest);
//    }

    private ModelCuaca parseHtmlKeModel(String response, int posisiRequest){
        //object model cuaca yang akan dikembalikan fungsi ini
        ModelCuaca modelCuaca = new ModelCuaca();
        //set unixtime
        modelCuaca.setUnixTime((System.currentTimeMillis() / 1000L) + ((posisiRequest-1)*(24*60*60)));
        //variabel document untuk parse html ke jsoup document
        Document document = Jsoup.parse(response);
        //element nama daerah
        Elements namaDaerahElement = document.select("body#forecast-extended " +
                "div#wrap div.sub-header-wrap " +
                "div.inner-city-header div#nav-main-add-interests " +
                "ul#nav-current-location li#current-city-tab " +
                "a.tab span.current-city ");
        //variabel utk menyimpan nama daerah
        String namaDaerah = "";
        //perulangan utk mendapatkan nama daerah
        for (Element row : namaDaerahElement){
            Elements innerDivs = row.select("h1");
            namaDaerah = innerDivs.get(0).text();
        }
        Log.d(Constant.TAG, "Jsoup namaDaerah->"+ namaDaerah);
        //set nama daerah ke model lokasi
        lokasi.setNamaDaerah(namaDaerah);

        //element lama hujan siang
        Elements lamaHujanSiangElemnt = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.day div.inner div.content ul.stats ");
        //variabel utk menampung hasil dari lama hujan siang
        String lamaHujanSiang = "";
        //perulangan utk mendapatkan lama hujan siang
        for (Element row : lamaHujanSiangElemnt){
            Elements innerDivs = row.select("li strong");
            lamaHujanSiang = innerDivs.get(7).text();
        }
        Log.d(Constant.TAG, "Jsoup lamaHujanSiang->"+ lamaHujanSiang.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
        modelCuaca.setLamaHujanSiang(Float.valueOf(lamaHujanSiang.replaceAll("[^\\d.]+|\\.(?!\\d)", "")));

        //elements lama hujan malam
        Elements lamaHujanMalamElement = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.night div.inner div.content ul.stats ");
        //variabel utk menampung hasil dari lama hujan malam
        String lamaHujanMalam = "";
        //perulangan utk mendapatkan lama hujan malam
        for (Element row : lamaHujanMalamElement){
            Elements innerDivs = row.select("li strong");
            lamaHujanMalam = innerDivs.get(7).text();
        }
        Log.d(Constant.TAG, "Jsoup lamaHujanMalam->"+ lamaHujanMalam.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
        modelCuaca.setLamaHujanMalam(Float.valueOf(lamaHujanMalam.replaceAll("[^\\d.]+|\\.(?!\\d)", "")));

        //elements suhu siang
        Elements suhuSiangelement = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.day div.inner div.info-wrapper div.info " +
                "div.temp span.large-temp ");
        //variabel utk menampung hasil suhu siang
        String suhuSiang = "";
        //perulangan utk mendapatkan suhu siang
        for (Element row : suhuSiangelement){
            suhuSiang = row.text();
        }
        Log.d(Constant.TAG, "Jsoup suhuSiang->"+ suhuSiang.replaceAll("\\D+",""));
        modelCuaca.setSuhuSiang(Integer.parseInt(suhuSiang.replaceAll("\\D+","")));

        //element suhu malam
        Elements suhuMalamElement = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.night div.inner div.info-wrapper " +
                "div.temp span.large-temp ");
        //variabel utk menampung suhu malam
        String suhuMalam = "";
        //perulangan utk membaca suhu malam
        for (Element row : suhuMalamElement){
            suhuMalam = row.text();
        }
        Log.d(Constant.TAG, "Jsoup suhuMalam->"+ suhuMalam.replaceAll("\\D+",""));
        modelCuaca.setSuhuMalam(Integer.parseInt(suhuMalam.replaceAll("\\D+","")));

        Elements anginSiangElement = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.day div.inner div.content " +
                "div.desc div.rt ul.wind-stats ");
        String anginSiang = "";
        for (Element row : anginSiangElement){
            anginSiang = row.text();
        }
        Log.d(Constant.TAG, "Jsoup anginSiang->"+ anginSiang);
        modelCuaca.setAnginSiang(anginSiang);

        Elements anginMalamElement = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.night div.inner div.content " +
                "div.desc div.rt ul.wind-stats ");
        String anginMalam = "";
        for (Element row : anginMalamElement){
            anginMalam = row.text();
        }
        Log.d(Constant.TAG, "Jsoup anginMalam->"+ anginMalam);
        modelCuaca.setAnginMalam(anginMalam);

        Elements detailSiangElement = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.day div.inner div.info-wrapper " +
                "div.info div.cond ");
        String detailSiang = "";
        for (Element row : detailSiangElement){
            detailSiang = row.text();
        }
        Log.d(Constant.TAG, "Jsoup detailSiang->"+ detailSiang);
        modelCuaca.setDetailSiang(detailSiang);

        Elements detailMalamElement = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.night div.inner div.info-wrapper " +
                "div.cond ");
        String detailMalam = "";
        for (Element row : detailMalamElement){
            detailMalam = row.text();
        }
        Log.d(Constant.TAG, "Jsoup detailMalam->"+ detailMalam);
        modelCuaca.setDetailMalam(detailMalam);

        //elemen curah hujan siang
        Elements curahHujanSiangElement = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.day div.inner div.content ul.stats  ");
        //variabel untuk menampung hasil dari curah hujan siang
        String curahHujanSiang = "";
        for (Element row : curahHujanSiangElement){
            Elements innerDivs = row.select("li strong");
            curahHujanSiang = innerDivs.get(3).text();
        }
        Log.d(Constant.TAG, "Jsoup curahHujanSiang->"+ curahHujanSiang.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
        modelCuaca.setCurahHujanSiang(Double.valueOf(curahHujanSiang.replaceAll("[^\\d.]+|\\.(?!\\d)", "")));

        //elements curah hujan malam
        Elements curahHujanMalamElement = document.select("body#forecast-extended " +
                "div#wrap div#takeover-wrap " +
                "div#wrap-content div#content div.column-1 " +
                "div#panel-main div.panel-body-lt div.panel-body-rt div.panel-body " +
                "div#details div#detail-day-night div.night div.inner div.content ul.stats ");
        //variabel utk menampung hasil dari curah hujan malam
        String curahHujanMalam = "";
        //perulangan utk mendapatkan curah hujan malam
        for (Element row : curahHujanMalamElement){
            Elements innerDivs = row.select("li strong");
            curahHujanMalam = innerDivs.get(3).text();
        }
        Log.d(Constant.TAG, "Jsoup curahHujanMalam->"+ curahHujanMalam.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
        modelCuaca.setCurahHujanMalam(Float.valueOf(curahHujanMalam.replaceAll("[^\\d.]+|\\.(?!\\d)", "")));

        return modelCuaca;
    }

    private String getUrl(String response){
        //parse response String dari volley sebagai Document Jsoup
        Document doc = Jsoup.parse(response);
        //mendapatkan List row by tag meta
        Elements rows = doc.getElementsByTag("meta");

        //variabel untuk menyimpan url nantinya
        String url = "";
        //perulangan untuk mendapatkan url yg valid
        for (Element row : rows){
            //menyimpan isi meta dengan atribut content
            String content = row.attr("content");
            //menyimpan isi meta dengan atribut name
            String name = row.attr("name");
            //jika isi dari name sama dengan isi dr content maka url yg di inginkan valid, dan siap disimpan ke variabel url
            if(name.equals("twitter:url")){
                url = content;
            }
        }

        //variabel untuk menyimpan kode nama daerah dan kode daerah
        String tempatKode = "";
        //char array untuk konversi dari String
        char[] temp = url.toCharArray();
        //hitung jumlah garis miring yg ditemukan
        int garisMiring = 0;
        for(int i = 0; i<temp.length; i++){
            if(temp[i] == '/'){
                garisMiring++;
            }
            if(garisMiring >=5 && garisMiring <7){
                //simpan nama daerah dan kode daerah jika sudah menemukan 5 garis miring dan <7 garis miring
                tempatKode += String.valueOf(temp[i]);
            }
        }

        //variabel utk menyimpan nama daerah hasil pemisahan dari tempatKode
        String daerah = "";
        //variabel utk menyimpan kode daerah hasil pemisahan dari tempatKode
        String kode = "";
        char[] temp2 = tempatKode.toCharArray();
        int garisMiring2 = 0;
        for(int i=0; i<temp2.length; i++){
            //jika garis miring 1 artinya simpan nama daerah
            if(garisMiring2 == 1){
                daerah += String.valueOf(temp2[i]);
            }
            //jika garis miring 2 artinya simpan kode daerah
            if(garisMiring2 == 2){
                kode += String.valueOf(temp2[i]);
            }
            //tambahkan jumlah garis miring setiap ditemukan baru
            if(temp2[i] == '/'){
                garisMiring2++;
            }
        }
        //replace garis miring dari nama daerah yang sudah disimpan
        daerah = daerah.replace("/", "");
        //kembalikan url cuaca fix siap utk grab data cuaca dari accu weather
        return "https://www.accuweather.com/id/id/"+daerah+"/"+kode+"/daily-weather-forecast/"+kode+"?day=";
    }

    private void simpanKeDatabase(){
        //pastikan jumlah data yang akan dimasukkan ke database 90
        if(listCuaca.size()==90) {
            int idLok = ModelLokasi.bacaIdByLokasi(databaseHelper.getOpenDatabase(), lokasi);
            if(idLok != -1){
                //jika lokasi ini sudah ada maka hapus dulu utk digantikan dengan lokasi yg baru
                databaseHelper.deleteData(new ModelLokasi(idLok));
            }

            if(namaLokasi!=null){
                lokasi.setNamaDaerah(namaLokasi);
            }

            int idLokasi = databaseHelper.insertData(lokasi);
            if(idLokasi!=-1) {
                for (ModelCuaca modelCuaca : listCuaca) {
                    Log.d(Constant.TAG, "insert cuaca");
                    modelCuaca.setIdLokasi(idLokasi);
                    databaseHelper.insertData(modelCuaca);
                }
                if (listenerCuaca != null)  //eksekusi onSelesaiDownload ketika semua data berhasil di download dan insert ke database
                    listenerCuaca.onSelesaiDownload(lokasi);
            }else{
                if (listenerCuaca != null)//terjadi kegagalan saat insert data ke tabel lokasi
                    listenerCuaca.onGagalDownload();
            }
        }else{// jika data tidak 90 artinya download data cuaca mengalami kegagalan
            if(listenerCuaca!=null)
                listenerCuaca.onGagalDownload();
        }
    }

    public void setListenerCuaca(ListenerCuaca listenerCuaca) {
        this.listenerCuaca = listenerCuaca;
    }

    public interface ListenerCuaca{
        void onSelesaiDownload(ModelLokasi lokasi);
        void onGagalDownload();
    }
}
