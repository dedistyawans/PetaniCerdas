<!-- Form Elements -->
                    <div class="panel panel-default">
                           <div class="panel-heading">
                                  Tambahkan Penyakit Tanaman
                            </div>
                             <div class="panel-body">
                                    <form role="form">
                                        <div class="form-group">
                                            <label>Nama Penyakit</label>
                                            <input id="nama_penyakit" class="form-control" placeholder="Masukkan Nama Penyakit Tanaman" maxlength="50"/>
                                            <div id="eror_nama_penyakit"></div>
                                        </div>
                                        <div class="form-group">
                                            <label>Deskripsi Penyakit (HTML)</label>
                                            <textarea id="deskripsi_penyakit" class="form-control" rows="10" placeholder="Masukkan deskripsi penyakit tanaman dalam format html"></textarea>
                                            <div id="eror_deskripsi_penyakit"></div>
                                        </div>
                                         <div class="form-group">
                                            <label>Ciri-Ciri Penyakit (HTML)</label>
                                            <textarea id="ciri_ciri_penyakit" class="form-control" rows="10" placeholder="Masukkan ciri-ciri penyakit tanaman dalam format html"></textarea>
                                            <div id="eror_ciri_ciri_penyakit"></div>
                                        </div>
                                          <div class="form-group">
                                            <label>Cara Menangani Penyakit (HTML)</label>
                                            <textarea id="cara_menangani_penyakit" class="form-control" rows="10" placeholder="Masukkan cara menangani penyakit tanaman dalam format html"></textarea>
                                            <div id="eror_cara_menangani_penyakit"></div>
                                        </div>
                                         <div class="form-group">
                                             <label>Pilih Tanaman</label>
                                            <select class="form-control" id="pilih_tanaman_option">
                                                <option value="Pilih Tanaman">Pilih Tanaman</option>
                                                <?php foreach ($tanaman as $data_tanaman) { ?>
                                                <option value="<?= $data_tanaman->id ?>"><?= $data_tanaman->nama ?></option>
                                                <?php } ?>
                                            </select>
                                            <div id="eror_pilih_tanaman_option"></div>
                                        </div>
                                        <div class="form-group">
                                            <label>Gambar Penyakit</label>
                                            <div class="dropzone" style="border: 2px dashed #0087F7;">
                                                    <div class="dz-message">
                                                              <h3>Klik Atau Drop Foto Penyakit Tanaman</h3>
                                                    </div>
                                            </div>
                                            <div id="eror_input_foto_penyakit"></div>
                                        </div>   
                                    </form>        
                                <button type="button" class="btn btn-primary" style="float: right" id="simpandatapenyakit">Simpan Data</button>
                             </div>
                   </div>

<script>    
        //simpan gambar yang sudah di upload dalam bentuk array
        var listGambarTeruploadPenyakit = [];
        //settingan library untuk tidak auto discover
        Dropzone.autoDiscover = false;
        //fungsi ajax library dropzone untuk upload multiple foto
        var foto_upload_penyakit= new Dropzone(".dropzone",{
                    url: "<?php echo base_url('index.php/Ajax/upload_foto_penyakit') ?>",
                    maxFilesize: 2,
                    method:"post",
                    acceptedFiles:"image/*",
                    paramName:"userfile",
                    dictInvalidFileType:"Type file ini tidak dizinkan",
                    addRemoveLinks:true,
                    success : function(file, response){
                        file.serverId = response; 
                        //simpan nama file yang berhasil diupload ke array
                        listGambarTeruploadPenyakit.push(response);
              }
        });

        //fungsi ajax library dropzone untuk menghapus foto
        foto_upload_penyakit.on("removedfile",function(file){
        //nama file foto yang akan di hapus di server
            var nama_file = file.serverId;
            $.ajax({
                type:"post",
                data:{nama_file:nama_file},
                url:"<?php echo base_url('index.php/Ajax/remove_foto_penyakit') ?>",
                cache:false,
                dataType: 'json',
                success: function(){
                        //mendapatkan index array berdasarkan value nama_file yang dihapus
                        var index = listGambarTeruploadPenyakit.indexOf(nama_file);
                        //jika index tidak -1 artinya item tersebut berada didalam array dan aman untuk di hapus
                        if(index !== -1){
                            //hapus isi array dengan index
                             listGambarTeruploadPenyakit.splice(index, 1);
                        }
                },
                error: function(){
                        //proses menghapus foto mengalami kegagalan dari sisi server 
                }
            });
        });
                
                
        $('#simpandatapenyakit').click(function(){
            if(validasi()){
                var nama_penyakit = $('#nama_penyakit').val();
                var deskripsi_penyakit = $('#deskripsi_penyakit').val();
                var ciri_ciri_penyakit = $('#ciri_ciri_penyakit').val();
                var cara_menangani_penyakit = $('#cara_menangani_penyakit').val();
                var option_pilih_tanaman = $('#pilih_tanaman_option').val();
                //tampilkan overlay loading
              $("#page-wrapper").LoadingOverlay("show", {
                  color   : "rgba(128,128,128, 0.8)"
               });  
                $.ajax({
                            url	: '<?= base_url(); ?>index.php/AjaxDbPenyakit/insert_ke_penyakit',
                            //mengirimkan username dan password ke  checklogin
                            data : {
                                nama : nama_penyakit ,
                                deskripsi : deskripsi_penyakit,
                                ciri : ciri_ciri_penyakit,
                                cara : cara_menangani_penyakit,
                                tanaman : option_pilih_tanaman,
                                foto : listGambarTeruploadPenyakit
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
                                              ajax('tambahkan_penyakit', 'AjaxPenyakit/tambah_penyakit');
                                    }else{                                     
                                            alert(pesan);    
                                    }
                            }
                    });
             }
        });

        function validasi(){
            var kembalian = true;
            if( $('#nama_penyakit').val() === ""){
                    $('#eror_nama_penyakit').html('<font color="red">nama penyakit tidak boleh kosong!</font>');
                    kembalian = false;
             }else{
                 $('#eror_nama_penyakit').html("");
             }
             if( $('#deskripsi_penyakit').val() === ""){
                    $('#eror_deskripsi_penyakit').html('<font color="red">Deskripsi penyakit tidak boleh kosong!</font>');
                    kembalian = false;
             }else{
                   $('#eror_deskripsi_penyakit').html("");
            }
             if( $('#ciri_ciri_penyakit').val() === ""){
                    $('#eror_ciri_ciri_penyakit').html('<font color="red">Ciri ciri penyakit tidak boleh kosong!</font>');
                    kembalian = false;
             }else{
                   $('#eror_ciri_ciri_penyakit').html("");
            }
             if( $('#cara_menangani_penyakit').val() === ""){
                    $('#eror_cara_menangani_penyakit').html('<font color="red">Cara menangani penyakit tidak boleh kosong!</font>');
                    kembalian = false;
             }else{
                   $('#eror_cara_menangani_penyakit').html("");
            }
              if( $('#pilih_tanaman_option').val() === "Pilih Tanaman"){
                    $('#eror_pilih_tanaman_option').html('<font color="red">Tanaman tidak boleh kosong!</font>');
                    kembalian = false;
             }else{
                   $('#eror_pilih_tanaman_option').html("");
            }
            if(listGambarTeruploadPenyakit.length<=0){
                  $('#eror_input_foto_penyakit').html('<font color="red">Foto penyakit tidak boleh kosong!</font>');
                   kembalian = false;
            }else{
                  $('#eror_input_foto_penyakit').html("");
            }
            return kembalian;
        }  
</script>
