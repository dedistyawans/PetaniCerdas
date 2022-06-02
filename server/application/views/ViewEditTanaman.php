          <div class="panel panel-default">
                   <div class="panel-heading">
                          Edit Data Tanaman
                    </div>
                     <div class="panel-body">
                            <form role="form">
                                <div class="form-group">
                                    <label>Pilih tanaman</label>
                                    <select class="form-control" id="pilih_id_tanaman_option">
                                        <?php if($aksi === 'edit_pilih') { ?>
                                            <?php foreach ($semua_tanaman as $tanaman) { ?>
                                                  <option value="<?= $tanaman->id ?>"><?= $tanaman->nama ?></option>
                                            <?php } ?>
                                        <?php } else { ?>
                                              <?php foreach ($semua_tanaman as $tanaman) { 
                                                       if($id_tanaman !== $tanaman->id){ ?>
                                                              <option value="<?= $tanaman->id ?>"><?= $tanaman->nama ?></option>
                                                      <?php } else{ ?>   
                                                            <option value="<?= $tanaman->id ?>" <?php echo 'selected="selected"'; ?>><?= $tanaman->nama ?></option>
                                                     <?php } ?> 
                                              <?php } ?>
                                        <?php }?>
                                    </select>
                                    <div id="eror_pilih_id_tanaman_option"></div>
                                </div>
                                <div class="form-group">
                                    <label>Nama tanaman</label>
                                    <input id="nama_tanaman" class="form-control" placeholder="Masukkan Nama tanaman" maxlength="50" />
                                    <div id="eror_nama_tanaman"></div>
                                </div>
                                <div class="form-group" >
                                    <label>Umur Tanaman (angka dalam hari)</label>
                                          <input id="umur_tanaman" type="number" min="1" max="180" class="form-control" placeholder="Masukkan Umur Tanaman Maksimal 180 hari" />
                                    <div id="eror_umur_tanaman"></div>
                                </div>
                                <div class="form-group">
                                    <label>Musim Tanaman</label>
                                    <select class="form-control" id="musim_tanaman">
                                        <option>Pilih Musim</option>
                                        <option>Musim Kemarau</option>
                                        <option>Musim Hujan</option> 
                                    </select>
                                    <div id="eror_musim_tanaman"></div>
                                </div>
                                  <div class="form-group">
                                    <label>Batas Bawah Ketinggian Tanah (angka dalam meter)</label>
                                    <input  id="batas_bawah_tanah" type="number" min="1" class="form-control" placeholder="Masukkan Batas Bawah Ketinggian Tanah" />
                                     <div id="eror_batas_bawah_tanah"></div>
                                  </div>
                                  <div class="form-group">
                                    <label>Batas Atas Ketinggian Tanah (angka dalam meter)</label>
                                    <input id="batas_atas_tanah"  type="number" min="1" class="form-control" placeholder="Masukkan Batas Atas Ketinggian Tanah" />
                                     <div id="eror_batas_atas_tanah"></div>
                                 </div>
                                  <div class="form-group">
                                   <label>Curah Hujan Minimal</label>
                                         <input id="curah_hujan_min"  type="number" min="1" class="form-control" placeholder="Masukkan Batas Minimal Curah Hujan" />
                                        </select>
                                         <div id="eror_curah_hujan_min"></div>
                                    </div>
                                     <div class="form-group">
                                        <label>Curah Hujan Maksimal</label>
                                         <input id="curah_hujan_max"  type="number" min="1" class="form-control" placeholder="Masukkan Batas Maksimal Cuarah Hujan" />
                                        </select>
                                         <div id="eror_curah_hujan_max"></div>
                                    </div>
                                    <div class="form-group">
                                        <label>Suhu Minimal (Celcius)</label>
                                         <input id="suhu_min"  type="number" min="1" class="form-control" placeholder="Masukkan Suhu Minimal Tanaman Ini Dalam Celcius" />
                                        </select>
                                         <div id="eror_suhu_min"></div>
                                    </div>
                                     <div class="form-group">
                                        <label>Suhu Maksimal (Celcius)</label>
                                         <input id="suhu_max"  type="number" min="1" class="form-control" placeholder="Masukkan Suhu Maksimal Tanaman Ini Dalam Celcius" />
                                        </select>
                                         <div id="eror_suhu_max"></div>
                                    </div>

                                <div class="form-group">
                                    <label>Deskripsi tanaman (HTML)</label>
                                    <textarea id="deskripsi_tanaman" class="form-control" rows="10" placeholder="Masukkan deskripsi tanaman dalam format html"></textarea>
                                    <div id="eror_deskripsi_tanaman"></div>
                                </div>
                                  <div class="form-group">
                                    <label>Rekomendasi Cara Menanam (HTML)</label>
                                    <textarea id="cara_menanam" class="form-control" rows="10" placeholder="Masukkan rekomendasi cara menanam yang baik dari tanaman tersebut dalam format html"></textarea>
                                    <div id="eror_cara_menanam"></div>
                                  </div>
                                <div class="form-group">
                                    <label>Jenis Tanah</label>
                                    <?php foreach ($tanah as $option) { ?>
                                    <div class="checkbox">
                                        <label>
                                            <input class="checkbox_tanah" type="checkbox" value="<?= $option->id ?>"  id="<?= $option->id ?>"><?= $option->nama ?>
                                        </label>
                                    </div>
                                    <?php } ?>
                                     <div id="eror_checkbox_tanah"></div>
                                </div>
                                <div class="form-group">
                                    <label>Gambar tanaman Saat Ini</label>
                                    <div id="area_edit_gambar_tanaman"> </div>
                                  </div>
                                <div class="form-group">
                                    <label>Tambahkan Gambar tanaman</label>
                                    <div class="dropzone" style="border: 2px dashed #0087F7;">
                                            <div class="dz-message">
                                                      <h3>Klik Atau Drop Foto tanaman Disini</h3>
                                            </div>
                                    </div>
                                </div>   
                            </form>        
                        <button type="button" class="btn btn-primary" style="float: right" id="simpandataedit">Simpan Data</button>
                     </div>
           </div>
<script>    
         <?php
              echo "var foto_tanaman_arr = ". json_encode($semua_foto_tanaman) . ";\n";
              echo "var data_tanaman_json = ". json_encode($semua_tanaman) . ";\n";
              echo "var tanah_terpilih_json = ". json_encode($tanah_terpilih) . ";\n";
              echo "var data_tanah_json = ". json_encode($tanah) . ";\n";
         ?>
    
        //variabel folder foto tanaman disimpan
        var folderFototanaman = '<?php echo base_url('assets/upload/foto-tanaman/') ?>';
        
        //secara default tampilkan data tanaman index 0 jika merupakan edit pilih
         <?php if($aksi === 'edit_pilih') { ?>
                ubah_data(0);
          <?php  }else{ ?>
                ubah_data($("select").prop("selectedIndex"));
          <?php  } ?>
        
        //handle on ketika seleksi di option berubah
         $("select").on("change", function() {
                var id = $(this).attr("id");
                //pastikan bahwa id adalah selec option untuk edit tanaman
                if(id === 'pilih_id_tanaman_option'){
                    var index = $(this).prop('selectedIndex');
                    ubah_data(index);
                }
          });
          
           //variabel untuk menyimpan id foto yang di hapus
           var idFototanamanTerhapus = [];
           
          //fungsi untuk merubah view ke index terpilih
          function ubah_data(index){
               //variabel untuk menyimpan id foto yang di hapus
               idFototanamanTerhapus = [];
               
              $('#nama_tanaman').val(data_tanaman_json[index]['nama']);
              $('#umur_tanaman').val(data_tanaman_json[index]['umur']);
              $('#musim_tanaman').val(data_tanaman_json[index]['musim']);
              $('#batas_bawah_tanah').val(data_tanaman_json[index]['ketinggian_min']);
              $('#batas_atas_tanah').val(data_tanaman_json[index]['ketinggian_max']);
              $('#curah_hujan_min').val(data_tanaman_json[index]['curah_hujan_min']);
              $('#curah_hujan_max').val(data_tanaman_json[index]['curah_hujan_max']);
              $('#suhu_min').val(data_tanaman_json[index]['suhu_min']);
              $('#suhu_max').val(data_tanaman_json[index]['suhu_max']);
              $('#deskripsi_tanaman').val(data_tanaman_json[index]['deskripsi']);
              $('#cara_menanam').val(data_tanaman_json[index]['rekomendasi_menanam']);
              
              for(i=0; i<data_tanah_json.length; i++){
                   $('#'+data_tanah_json[i]['id']).prop('checked', false);
              }
              
              for(i=0; i<tanah_terpilih_json[index].length; i++){
                   for(j=0; j<data_tanah_json.length; j++){
                            if(tanah_terpilih_json[index] [i] ['id_tanah'] === data_tanah_json [j] ['id'] ){
                                      $('#'+data_tanah_json[j]['id']).prop('checked', true);
                             }
                   }
              }
              
              var listGambarHtmltanaman = '';
              for(i=0; i<foto_tanaman_arr[index][1].length; i++){
                   listGambarHtmltanaman += '<div class="img-wrap ';
                   listGambarHtmltanaman += foto_tanaman_arr[index][0][i];
                   listGambarHtmltanaman += '" style="margin: 5px;">';
                   listGambarHtmltanaman += '<span class="close">&times;</span>';
                   listGambarHtmltanaman += '<img src="';
                   listGambarHtmltanaman += folderFototanaman;
                   listGambarHtmltanaman += foto_tanaman_arr[index][1][i];
                   listGambarHtmltanaman += '" width="198px" height="198px" data-id="';
                   listGambarHtmltanaman += foto_tanaman_arr[index][0][i];
                   listGambarHtmltanaman += '">';
                   listGambarHtmltanaman += '</div>';
               }
                  
               //ubah image
              $('#area_edit_gambar_tanaman').html(listGambarHtmltanaman);
              
              //handle click pada close button (berfungsi untuk delete gambar)
                $('.img-wrap .close').on('click', function() {
                    if((idFototanamanTerhapus.length+1) >= foto_tanaman_arr[index][0].length){
                          alert("Gagal : Tak dapat menghapus semua foto, minimal harus tersisa 1!");
                     }else{
                            var id = $(this).closest('.img-wrap').find('img').data('id');
                            idFototanamanTerhapus.push(id);
                            $( "."+id ).hide('slow', function(){ $( "."+id ).remove(); });
                     }
               });
          }
              
        //simpan gambar yang sudah di upload dalam bentuk array
        var listGambarTeruploadtanaman = [];
        //settingan library untuk tidak auto discover
        Dropzone.autoDiscover = false;
        //fungsi ajax library dropzone untuk upload multiple foto
        var foto_upload_tanaman= new Dropzone(".dropzone",{
                    url: "<?php echo base_url('index.php/Ajax/upload_foto_tanaman') ?>",
                    maxFilesize: 2,
                    method:"post",
                    acceptedFiles:"image/*",
                    paramName:"userfile",
                    dictInvalidFileType:"Type file ini tidak dizinkan",
                    addRemoveLinks:true,
                    success : function(file, response){
                        file.serverId = response; 
                        //simpan nama file yang berhasil diupload ke array
                        listGambarTeruploadtanaman.push(response);
              }
        });

        //fungsi ajax library dropzone untuk menghapus foto
        foto_upload_tanaman.on("removedfile",function(file){
        //nama file foto yang akan di hapus di server
            var nama_file = file.serverId;
            $.ajax({
                type:"post",
                data:{nama_file:nama_file},
                url:"<?php echo base_url('index.php/Ajax/remove_foto_tanaman') ?>",
                cache:false,
                dataType: 'json',
                success: function(){
                        //mendapatkan index array berdasarkan value nama_file yang dihapus
                        var index = listGambarTeruploadtanaman.indexOf(nama_file);
                        //jika index tidak -1 artinya item tersebut berada didalam array dan aman untuk di hapus
                        if(index !== -1){
                            //hapus isi array dengan index
                             listGambarTeruploadtanaman.splice(index, 1);
                        }
                },
                error: function(){
                        //proses menghapus foto mengalami kegagalan dari sisi server 
                }
            });
        });
                
           //untuk menimpan data checkbox tanah yang sudah dipilih
          var checkbox_tanah_terpilih = [];
                
        $('#simpandataedit').click(function(){ 
              //lakukan check ke checkbox tanah dan simpan semua checkbox yang dipilih
            checkbox_tanah_terpilih = $('.checkbox_tanah:checked').map(function () {
                return this.value;
            }).get();
        
            if(validasi()){      
                var id_t = $('#pilih_id_tanaman_option').val();
                var nama_tanaman = $('#nama_tanaman').val();
                var umur_tanaman = $('#umur_tanaman').val();
                var musim_tanaman = $('#musim_tanaman').val();
                var ketinggian_min =  $('#batas_bawah_tanah').val();
                var ketinggian_max = $('#batas_atas_tanah').val();
                var curah_hujan_min = $('#curah_hujan_min').val();
                var curah_hujan_max = $('#curah_hujan_max').val();
                var suhu_min = $('#suhu_min').val();
                var suhu_max = $('#suhu_max').val();
                var deskripsi_tanaman = $('#deskripsi_tanaman').val();
                var rekomendasi_menanam = $('#cara_menanam').val();
               
               //tampilkan overlay loading
              $("#page-wrapper").LoadingOverlay("show", {
                  color   : "rgba(128,128,128, 0.8)"
               });  
                $.ajax({
                        url : '<?= base_url(); ?>index.php/AjaxDbTanaman/edit_ke_tanaman',
                        //mengirimkan data yang diperlukan untuk edit tanaman
                        data : {
                                id : id_t,
                                nama : nama_tanaman,
                                umur : umur_tanaman,
                                musim : musim_tanaman,
                                ketinggian_min : ketinggian_min,
                                ketinggian_max : ketinggian_max,
                                curah_hujan_min : curah_hujan_min,
                                curah_hujan_max : curah_hujan_max,
                                suhu_min : suhu_min,
                                suhu_max : suhu_max,
                                deskripsi : deskripsi_tanaman,  
                                rekomendasi_menanam : rekomendasi_menanam,
                                tanah : checkbox_tanah_terpilih,
                                foto_baru : listGambarTeruploadtanaman,
                                foto_terhapus : idFototanamanTerhapus
                        }, 
                        //Method pengiriman
                        type : 'POST',
                        //Data yang akan diambil dari script pemroses
                        dataType: 'html',
                        //Respon jika data berhasil dikirim
                        success : function(pesan){     
                            //hide overlay loading
                               $("#page-wrapper").LoadingOverlay("hide");   
                                if(pesan==='sukses'){ 
                                         alert("Data berhasil di simpan");
                                          ajaxWithId(id_t);
                                }else{                                     
                                        alert(pesan);    
                                }
                        }
                    });
             }else{
                alert('Gagal : harap perbaiki form yang ditandai dengan eror warna merah');
             }
        });
        
        function ajaxWithId(id_t){
             //tampilkan overlay loading
              $("#page-wrapper").LoadingOverlay("show", {
                  color   : "rgba(128,128,128, 0.8)"
               });  
                $.ajax({
                  url : '<?= base_url(); ?>index.php/AjaxTanaman/edit_tanaman',
                  //mengirimkan data ke ajax edit tanaman
                  data : {
                      id: id_t
                  }, 
                  //Method pengiriman
                  type : 'POST',
                  //Data yang akan diambil dari script pemroses
                  dataType: 'html',
                  //Respon jika data berhasil dikirim
                  success : function(pesan){
                          $("#page-wrapper").html(pesan);
                          $("#page-wrapper").LoadingOverlay("hide");    
                  }
              });
        }

        function validasi(){
              var kembalian = true;
                if( $('#nama_tanaman').val() === ""){
                        $('#eror_nama_tanaman').html('<font color="red">Nama tanaman tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                     $('#eror_nama_tanaman').html("");
                 }
                 if( $('#umur_tanaman').val() === ""){
                        $('#eror_umur_tanaman').html('<font color="red">Umur tanaman tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_umur_tanaman').html("");
                }
                 if( $('#musim_tanaman').val() === "Pilih Musim"){
                        $('#eror_musim_tanaman').html('<font color="red">Musim tanaman tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_musim_tanaman').html("");
                }
                 if( $('#batas_bawah_tanah').val() === ""){
                        $('#eror_batas_bawah_tanah').html('<font color="red">Batas bawah ketinggian tanah tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_batas_bawah_tanah').html("");
                }
                if( $('#batas_atas_tanah').val() === ""){
                        $('#eror_batas_atas_tanah').html('<font color="red">Batas atas ketinggian tanah tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_batas_atas_tanah').html("");
                }
                 if( $('#curah_hujan_min').val() === ""){
                        $('#eror_curah_hujan_min').html('<font color="red">Curah hujan minimal tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_curah_hujan_min').html("");
                }
                if( $('#curah_hujan_max').val() === ""){
                        $('#eror_curah_hujan_max').html('<font color="red">Curah hujan maksimal tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_curah_hujan_max').html("");
                }
                 if( $('#suhu_min').val() === ""){
                        $('#eror_suhu_min').html('<font color="red">Suhu minimal tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_suhu_min').html("");
                }
                if( $('#suhu_max').val() === ""){
                        $('#eror_suhu_max').html('<font color="red">Suhu maksimal tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_suhu_max').html("");
                }
                if( $('#deskripsi_tanaman').val() === ""){
                        $('#eror_deskripsi_tanaman').html('<font color="red">Deskripsi tanaman tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_deskripsi_tanaman').html("");
                }
                 if( $('#cara_menanam').val() === ""){
                        $('#eror_cara_menanam').html('<font color="red">Cara menanam tak boleh kosong!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_cara_menanam').html("");
                }
                 if(checkbox_tanah_terpilih.length<=0){
                        $('#eror_checkbox_tanah').html('<font color="red">Pilih setidaknya satu jenis tanah untuk tanaman ini!</font>');
                        kembalian = false;
                 }else{
                       $('#eror_checkbox_tanah').html("");
                }
                return kembalian;
        }  
</script>
