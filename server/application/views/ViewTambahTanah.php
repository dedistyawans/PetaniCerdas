<!-- Form Elements -->
                    <div class="panel panel-default">
                           <div class="panel-heading">
                                  Tambahkan Tanah
                            </div>
                             <div class="panel-body">
                                    <form role="form">
                                        <div class="form-group">
                                            <label>Nama Tanah</label>
                                            <input id="nama_tanah" class="form-control" placeholder="Masukkan Nama Tanah" maxlength="50"/>
                                            <div id="eror_nama_tanah"></div>
                                        </div>
                                          <div class="form-group">
                                            <label>Deskripsi Tanah (HTML)</label>
                                            <textarea id="deskripsi_tanah" class="form-control" rows="10" placeholder="Masukkan deskripsi tanah dalam format html"></textarea>
                                            <div id="eror_deskripsi_tanah"></div>
                                          </div>
                                        <div class="form-group">
                                            <label>Gambar Tanah</label>
                                            <div class="dropzone" style="border: 2px dashed #0087F7;">
                                                    <div class="dz-message">
                                                              <h3>Klik Atau Drop Foto Tanah Disini</h3>
                                                    </div>
                                            </div>
                                            <div id="eror_input_foto_tanah"></div>
                                        </div>   
                                    </form>        
                                <button type="button" class="btn btn-primary" style="float: right" id="simpandata">Simpan Data</button>
                             </div>
                        
                   </div>

<script>    
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
                
                
        $('#simpandata').click(function(){
            if(validasi()){
                var nama = $('#nama_tanah').val();
                var deskripsi = $('#deskripsi_tanah').val();
                   //tampilkan overlay loading
              $("#page-wrapper").LoadingOverlay("show", {
                  color   : "rgba(128,128,128, 0.8)"
               });  
                $.ajax({
                            url	: '<?= base_url(); ?>index.php/AjaxDbTanah/insert_ke_tanah',
                            //mengirimkan username dan password ke  checklogin
                            data : {nama : nama , deskripsi : deskripsi, foto : listGambarTeruploadTanah}, 
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
                                             ajax('tambahkan_tanah', 'AjaxTanah/tambah_tanah');
                                    }else{                                     
                                            alert("Data gagal dimasukkan");    
                                    }
                            }
                    });
             }
        });

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
            if(listGambarTeruploadTanah.length<=0){
                  $('#eror_input_foto_tanah').html('<font color="red">Foto tanah tidak boleh kosong!</font>');
                   kembalian = false;
            }else{
                  $('#eror_input_foto_tanah').html("");
            }
            return kembalian;
        }  
</script>
