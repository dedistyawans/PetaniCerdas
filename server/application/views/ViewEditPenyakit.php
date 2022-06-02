<!-- Form Elements -->
                    <div class="panel panel-default">
                           <div class="panel-heading">
                                  Edit Data Penyakit Tanaman
                            </div>
                             <div class="panel-body">
                                    <form role="form">
                                        <div class="form-group">
                                             <label>Pilih Penyakit</label>
                                            <select class="form-control" id="pilih_id_penyakit_option">
                                               <?php if($aksi === 'edit_pilih') { ?>
                                                   <?php foreach ($data_penyakit as $penyakit) { ?>
                                                            <option value="<?= $penyakit->id ?>"><?= $penyakit->nama ?></option>
                                                    <?php } ?>
                                                <?php } else { ?>
                                                      <?php foreach ($data_penyakit as $penyakit) { 
                                                               if($id_penyakit !== $penyakit->id){ ?>
                                                                      <option value="<?= $penyakit->id ?>"><?= $penyakit->nama ?></option>
                                                              <?php } else{ ?>   
                                                                    <option value="<?= $penyakit->id ?>" <?php echo 'selected="selected"'; ?>><?= $penyakit->nama ?></option>
                                                             <?php } ?> 
                                                      <?php } ?>
                                                <?php }?>
                                            </select>
                                        </div>
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
                                                <?php foreach ($data_tanaman as $tanaman) { ?>
                                                <option value="<?= $tanaman->id ?>"><?= $tanaman->nama ?></option>
                                                <?php } ?>
                                            </select>
                                            <div id="eror_pilih_tanaman_option"></div>
                                        </div>
                                        <div class="form-group">
                                            <label>Gambar Penyakit Saat Ini</label>
                                            <div id="area_edit_gambar_penyakit"> </div>
                                        </div>
                                        <div class="form-group">
                                            <label>Gambar Penyakit</label>
                                            <div class="dropzone" style="border: 2px dashed #0087F7;">
                                                    <div class="dz-message">
                                                              <h3>Klik Atau Drop Foto Penyakit Tanaman</h3>
                                                    </div>
                                            </div>
                                        </div>   
                                    </form>        
                                <button type="button" class="btn btn-primary" style="float: right" id="simpandatapenyakit">Simpan Data</button>
                             </div>
                   </div>

<script>    
    
        //variabel variabel yang diambil dari database dan disimpan dalam javascript
         <?php
              echo "var foto_penyakit_json = ". json_encode($foto_penyakit) . ";\n";
              echo "var data_penyakit_json = ". json_encode($data_penyakit) . ";\n";
         ?>
             
         //variabel folder foto tanaman disimpan
         var folderFotoPenyakit = '<?php echo base_url('assets/upload/foto-penyakit/') ?>';
        
        //secara default tampilkan data tanaman index 0 jika merupakan edit pilih
         <?php if($aksi === 'edit_pilih') { ?>
                ubah_data(0);
          <?php  }else{ ?>
                ubah_data($("#pilih_id_penyakit_option").prop("selectedIndex"));
          <?php  } ?>
        
        //handle on ketika seleksi di option berubah
         $("select").on("change", function() {
                var id = $(this).attr("id");
                //pastikan bahwa id adalah selec option untuk edit tanaman
                if(id === 'pilih_id_penyakit_option'){
                    var index = $(this).prop('selectedIndex');
                    ubah_data(index);
                }
          });
          
           //variabel untuk menyimpan id foto yang di hapus
           var idFotoPenyakitTerhapus = [];
          
          //fungsi untuk merubah view ke index terpilih
          function ubah_data(index){
               //variabel untuk menyimpan id foto yang di hapus
               idFotoPenyakitTerhapus = [];
               
             $('#nama_penyakit').val(data_penyakit_json[index]['nama']);
             $('#deskripsi_penyakit').val(data_penyakit_json[index]['deskripsi']);
             $('#ciri_ciri_penyakit').val(data_penyakit_json[index]['ciri_ciri']);
             $('#cara_menangani_penyakit').val(data_penyakit_json[index]['cara_menangani']);
             $('#pilih_tanaman_option').val(data_penyakit_json[index]['id_tanaman']);
              
             var listGambarHtmlPenyakit = '';
             for(i=0; i<foto_penyakit_json[index].length; i++){
                      listGambarHtmlPenyakit += '<div class="img-wrap ';
                      listGambarHtmlPenyakit += foto_penyakit_json[index][i]['id'];
                      listGambarHtmlPenyakit += '" style="margin: 5px;">';
                      listGambarHtmlPenyakit += '<span class="close">&times;</span>';
                      listGambarHtmlPenyakit += '<img src="';
                      listGambarHtmlPenyakit += folderFotoPenyakit;
                      listGambarHtmlPenyakit += foto_penyakit_json[index][i]['nama_file'];
                      listGambarHtmlPenyakit += '" width="198px" height="198px" data-id="';
                      listGambarHtmlPenyakit += foto_penyakit_json[index][i]['id'];
                      listGambarHtmlPenyakit += '">';
                      listGambarHtmlPenyakit += '</div>';
              }

              //ubah image
              $('#area_edit_gambar_penyakit').html(listGambarHtmlPenyakit);
              
              //handle click pada close button (berfungsi untuk delete gambar)
                $('.img-wrap .close').on('click', function() {
                    if((idFotoPenyakitTerhapus.length+1) >= foto_penyakit_json[index].length){
                          alert("Gagal : Tak dapat menghapus semua foto, minimal harus tersisa 1 foto!");
                     }else{
                            var id = $(this).closest('.img-wrap').find('img').data('id');
                            idFotoPenyakitTerhapus.push(id);
                            $( "."+id ).hide('slow', function(){ $( "."+id ).remove(); });
                     }
               });
          }
    
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
                var id = $('#pilih_id_penyakit_option').val();
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
                            url	: '<?= base_url(); ?>index.php/AjaxDbPenyakit/edit_ke_penyakit',
                            //mengirimkan username dan password ke  checklogin
                            data : {
                                id : id,
                                nama : nama_penyakit ,
                                deskripsi : deskripsi_penyakit,
                                ciri : ciri_ciri_penyakit,
                                cara : cara_menangani_penyakit,
                                tanaman : option_pilih_tanaman,
                                foto_terhapus : idFotoPenyakitTerhapus,
                                foto_baru : listGambarTeruploadPenyakit
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
             }else{
                    alert('Gagal : harap perbaiki form yang diberi tanda merah!');
             }
        });
        
       function ajaxWithId(id){
             //tampilkan overlay loading
              $("#page-wrapper").LoadingOverlay("show", {
                  color   : "rgba(128,128,128, 0.8)"
               });  
                $.ajax({
                  url : '<?= base_url(); ?>index.php/AjaxPenyakit/edit_penyakit',
                  //mengirimkan data ke ajax penyakit
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
            if( $('#nama_penyakit').val() === ""){
                    $('#eror_nama_penyakit').html('<font color="red">Nama penyakit tidak boleh kosong!</font>');
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
           
            return kembalian;
        }  
</script>
