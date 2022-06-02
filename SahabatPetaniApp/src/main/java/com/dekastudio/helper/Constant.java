package com.dekastudio.helper;


import com.dekastudio.model.ModelFotoPenyakit;
import com.dekastudio.model.ModelFotoTanah;
import com.dekastudio.model.ModelFotoTanaman;
import com.dekastudio.model.ModelPenyakit;
import com.dekastudio.model.ModelTanah;
import com.dekastudio.model.ModelTanaman;
import com.dekastudio.model.ModelTanamanTanah;

public class Constant {

    //Google Maps Elevation API URL
    //sample : https://maps.googleapis.com/maps/api/elevation/json?locations=39.7391536,-104.9847034&key=AIzaSyCDNY407s0882uZ4ZSxiINsLESLzVQY38g
    public static final String ELEVATION_API_URL = "https://maps.googleapis.com/maps/api/elevation/json?locations=";

    //dokumentasi https://developers.google.com/maps/documentation/geocoding/start?hl=en_US
    //url Google Maps Geocoding API, limit 2500/day dan 50/s utk free -> https://developers.google.com/maps/documentation/geocoding/usage-limits?hl=en_US
    //sample https://maps.googleapis.com/maps/api/geocode/json?address=alun+alun+magelang&key=AIzaSyCDNY407s0882uZ4ZSxiINsLESLzVQY38g
    public static final String GEOCODING_API_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    //api key Google Maps Geocoding API
    public static final String GOOGLE_API_KEY = "&key=AIzaSyCDNY407s0882uZ4ZSxiINsLESLzVQY38g";

    //url tempat dimana data prakiraan cuaca 90 hari kedepan di dapatkan
    public static final String ACCU_WEATHER_URL = "https://www.accuweather.com/id/search-locations";

    //TAG digunakan utk Log dari semua class dan activity
    public static final String TAG = "DekaTag";

    //untuk intent ketinggian tanah
    public static final String TINGGI_TANAH_CODE = "tinggicode";
    //untuk intent curah hujan
    public static final String CURAH_HUJAN_CODE = "curahcode";
    //untuk intent suhu
    public static final String SUHU_CODE = "suhucode";
    //untuk intent idTanah
    public static final String ID_TANAH_CODE = "idtanahcode";
    //untuk intent index
    public static final String INDEX_CODE = "indexcode";

    //untuk database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "petanicerdas";

    //untuk nama json versi disisi server
    public static final String API_UPDATE = "update";
    public static final String API_DATA_UPDATE = "data";
    public static final String API_VERSI_SERVER = "versi";
    public static final String API_AKSI = "aksi";
    public static final String API_NAMA_TABEL = "nama_tabel";
    public static final String API_ID_DATA = "id_data";

    //untuk preference
    public static final String PREF_APP_VERSION = "versiapp";

    //alamat2 yangdibutuhkan untuk API database
    private static final String SERVER_URL = "http://petanicerdas.khanzadie.com/";
    public static final String API_URL = SERVER_URL + "index.php/api/";
    public static final String API_VERSION_URL = API_URL + "update/";

    //alamat api utk tabel2
    public static final String API_TANAMAN_URL = API_URL + ModelTanaman.TABLE_NAME +"/";
    public static final String API_TANAH_URL = API_URL + ModelTanah.TABLE_NAME + "/";
    public static final String API_TANAMAN_TANAH_URL = API_URL + ModelTanamanTanah.TABLE_NAME + "/";
    public static final String API_PENYAKIT_URL = API_URL + ModelPenyakit.TABLE_NAME + "/";
    public static final String API_FOTO_TANAMAN_URL = API_URL + ModelFotoTanaman.TABLE_NAME + "/";
    public static final String API_FOTO_TANAH_URL = API_URL + ModelFotoTanah.TABLE_NAME + "/";
    public static final String API_FOTO_PENYAKIT_URL = API_URL + ModelFotoPenyakit.TABLE_NAME + "/";

    //alamat2 utk gambar
    public static final String FOTO_TANAMAN_URL = SERVER_URL + "assets/upload/foto-tanaman/";
    public static final String FOTO_TANAH_URL = SERVER_URL + "assets/upload/foto-tanah/";
    public static final String FOTO_PENYAKIT_URL = SERVER_URL + "assets/upload/foto-penyakit/";

    ///nama folder sdcard utk menyimpan gambar secara offline
    private static final String ROOT_FOLDER = "sahabat-petani";
    public static final String TANAMAN_FOLDER = ROOT_FOLDER + "/" + "tanaman";
    public static final String TANAH_FOLDER = ROOT_FOLDER + "/" + "tanah";
    public static final String PENYAKIT_FOLDER = ROOT_FOLDER + "/" + "penyakit";

    //macam2 aksi pada database
    public static final String AKSI_INSERT = "insert";
    public static final String AKSI_UPDATE = "update";
    public static final String AKSI_DELETE = "delete";

}
