<script type='text/javascript'>
    <?php
            $arr_nama_tanah = array();
            $arr_deskripsi_tanah = array();
            for($i = 0; $i<count($semua_tanah); $i++){
                $arr_nama_tanah[$i] = $semua_tanah[$i]->nama;
                $arr_deskripsi_tanah[$i] = $semua_tanah[$i]->deskripsi;
            }
             
            $js_array_nama_tanah = json_encode($arr_nama_tanah);
            $js_array_deskripsi_tanah = json_encode($arr_deskripsi_tanah);
            $js_array_foto_tanah = json_encode($semua_foto_tanah);
            echo "var nama_tanah_arr = ". $js_array_nama_tanah . ";\n";
            echo "var deskripsi_tanah_arr = ". $js_array_deskripsi_tanah . ";\n";
            echo "var foto_tanah_arr = ". $js_array_foto_tanah . ";\n";
    ?>
</script>

<!-- Form Elements -->
                    <div class="panel panel-default">
                           <div class="panel-heading">
                                  Edit Data Tanah
                            </div>
                             <div class="panel-body">
                                    <form role="form">
                                        <div class="form-group">
                                            <label>Pilih Tanah</label>
                                            <select class="form-control" id="pilih_id_tanah_option">
                                                <?php if($aksi === 'edit_pilih') { ?>
                                                    <?php foreach ($semua_tanah as $tanah) { ?>
                                                          <option value="<?= $tanah->id ?>"><?= $tanah->nama ?></option>
                                                    <?php } ?>
                                                <?php } else { ?>
                                                      <?php foreach ($semua_tanah as $tanah) { 
                                                               if($id_tanah !== $tanah->id){ ?>
                                                                      <option value="<?= $tanah->id ?>"><?= $tanah->nama ?></option>
                                                              <?php } else{ ?>   
                                                                    <option value="<?= $tanah->id ?>" <?php echo 'selected="selected"'; ?>><?= $tanah->nama ?></option>
                                                             <?php } ?> 
                                                      <?php } ?>
                                                <?php }?>
                                            </select>
                                            <div id="eror_pilih_id_tanah_option"></div>
                                        </div>
                                        <div class="form-group">
                                            <label>Nama Tanah</label>
                                            <input id="nama_tanah" class="form-control" placeholder="Masukkan Nama Tanah" maxlength="50" />
                                            <div id="eror_nama_tanah"></div>
                                        </div>
                                        <div class="form-group">
                                            <label>Deskripsi Tanah (HTML)</label>
                                            <textarea id="deskripsi_tanah" class="form-control" rows="10" placeholder="Masukkan deskripsi tanah dalam format html"></textarea>
                                            <div id="eror_deskripsi_tanah"></div>
                                        </div>
                                        <div class="form-group">
                                            <label>Gambar Tanah Saat Ini</label>
                                            <div id="area_edit_gambar_tanah"> </div>
                                          </div>
                                        <div class="form-group">
                                            <label>Tambahkan Gambar Tanah</label>
                                            <div class="dropzone" style="border: 2px dashed #0087F7;">
                                                    <div class="dz-message">
                                                              <h3>Klik Atau Drop Foto Tanah Disini</h3>
                                                    </div>
                                            </div>
                                        </div>   
                                    </form>        
                                <button type="button" class="btn btn-primary" style="float: right" id="simpandataedit">Simpan Data</button>
                             </div>
                   </div>

<script>    
        //variabel folder foto tanah disimpan
        var folderFotoTanah = '<?php echo base_url('assets/upload/foto-tanah/') ?>';
        
        //secara default tampilkan data tanah index 0 jika merupakan edit pilih
         <?php if($aksi === 'edit_pilih') { ?>
                ubah_data(0);
          <?php  }else{ ?>
                ubah_data($("select").prop("selectedIndex"));
          <?php  } ?>
        
        //handle on ketika seleksi di option berubah
         $("select").on("change", function() {
                var id = $(this).attr("id");
                //pastikan bahwa id adalah selec option untuk edit tanah
                if(id === 'pilih_id_tanah_option'){
                    var index = $(this).prop('selectedIndex');
                    ubah_data(index);
                }
          });
          
           //variabel untuk menyimpan id foto yang di hapus
           var idFotoTanahTerhapus = [];
          
          //fungsi untuk merubah view ke index terpilih
          function ubah_data(index){
               //variabel untuk menyimpan id foto yang di hapus
               idFotoTanahTerhapus = [];
              
              $('#nama_tanah').val(nama_tanah_arr[index]);
              $('#deskripsi_tanah').val(deskripsi_tanah_arr[index]);
              
              var listGambarHtmlTanah = '';
              for(i=0; i<foto_tanah_arr[index][1].length; i++){
                   listGambarHtmlTanah += '<div class="img-wrap ';
                   listGambarHtmlTanah += foto_tanah_arr[index][0][i];
                   listGambarHtmlTanah += '" style="margin: 5px;">';
                   listGambarHtmlTanah += '<span class="close">&times;</span>';
                   listGambarHtmlTanah += '<img src="';
                   listGambarHtmlTanah += folderFotoTanah;
                   listGambarHtmlTanah += foto_tanah_arr[index][1][i];
                   listGambarHtmlTanah += '" width="198px" height="198px" data-id="';
                   listGambarHtmlTanah += foto_tanah_arr[index][0][i];
                   listGambarHtmlTanah += '">';
                   listGambarHtmlTanah += '</div>';
               }
                  
               //ubah image
              $('#area_edit_gambar_tanah').html(listGambarHtmlTanah);
              
              //handle click pada close button (berfungsi untuk delete gambar)
                $('.img-wrap .close').on('click', function() {
                    if((idFotoTanahTerhapus.length+1) >= foto_tanah_arr[index][0].length){
                          alert("Gagal : Tak dapat menghapus semua foto, minimal harus tersisa 1!");
                     }else{
                            var id = $(this).closest('.img-wrap').find('img').data('id');
                            idFotoTanahTerhapus.push(id);
                            $( "."+id ).hide('slow', function(){ $( "."+id ).remove(); });
                     }
               });
          }
              
        //simpan gambar yang sudah di upload dalam bentuk array
        var listGambarTeruploadTanah = [];
        //settingan library untuk tidak auto discover
        Dropzone.autoDiscover = false;
        //fungsi ajax library dropzone untuk upload multiple foto
        var foto_upload_tanah= new Dropzone(".dropzone",{
                    url: "<?php echo base_url('index.php/Ajax/upload_foto_tanah') ?>",
                    maxFilesize: 2,
                    method:"post",
                    acceptedFiles:"image/*",
                    paramName:"userfile",
                    dictInvalidFileType:"Type file ini tidak dizinkan",
                    addRemoveLinks:true,
                    success : function(file, response){
                        file.serverId = response; 
                        //simpan nama file yang berhasil diupload ke array
                        listGambarTeruploadTanah.push(response);
              }
        });

        //fungsi ajax library dropzone untuk menghapus foto
        foto_upload_tanah.on("removedfile",function(file){
        //nama file foto yang akan di hapus di server
            var nama_file = file.serverId;
            $.ajax({
                type:"post",
                data:{nama_file:nama_file},
                url:"<?php echo base_url('index.php/Ajax/remove_foto_tanah') ?>",
                cache:false,
                dataType: 'json',
                success: function(){
                        //mendapatkan index array berdasarkan value nama_file yang dihapus
                        var index = listGambarTeruploadTanah.indexOf(nama_file);
                        //jika index tidak -1 artinya item tersebut berada didalam array dan aman untuk di hapus
                        if(index !== -1){
                            //hapus isi array dengan index
                             listGambarTeruploadTanah.splice(index, 1);
                        }
                },
                error: function(){
                        //proses menghapus foto mengalami kegagalan dari sisi server 
                }
            });
        });
                
                
        $('#simpandataedit').click(function(){ 
            if(validasi()){      
                var id = $('#pilih_id_tanah_option').val();
                var nama = $('#nama_tanah').val();
                var deskripsi = $('#deskripsi_tanah').val();
               
               //tampilkan overlay loading
              $("#page-wrapper").LoadingOverlay("show", {
                  color   : "rgba(128,128,128, 0.8)"
               });  
                $.ajax({
                        url : '<?= base_url(); ?>index.php/AjaxDbTanah/edit_ke_tanah',
                        //mengirimkan data
                        data : {
                            id: id,
                            nama : nama,
                            deskripsi : deskripsi,
                            foto_terhapus : idFotoTanahTerhapus,
                            foto_baru : listGambarTeruploadTanah
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
                                          ajaxWithId(id);
                                }else{                                     
                                        alert(pesan);    
                                }
                        }
                    });
             }
        });
        
        function ajaxWithId(id){
             //tampilkan overlay loading
              $("#page-wrapper").LoadingOverlay("show", {
                  color   : "rgba(128,128,128, 0.8)"
               });  
                $.ajax({
                  url : '<?= base_url(); ?>index.php/AjaxTanah/edit_tanah',
                  //mengirimkan username dan password ke  checklogin
                  data : {
                      id: id
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
            if( $('#nama_tanah').val() === ""){
                    $('#eror_nama_tanah').html('<font color="red">Nama tanah tak boleh kosong!</font>');
                    kembalian = false;
             }else{
                 $('#eror_nama_tanah').html("");
             }
             if( $('#deskripsi_tanah').val() === ""){
                    $('#eror_deskripsi_tanah').html('<font color="red">Deskripsi tanah tak boleh kosong!</font>');
                    kembalian = false;
             }else{
                   $('#eror_deskripsi_tanah').html("");
            }
            return kembalian;
        }  
</script>
