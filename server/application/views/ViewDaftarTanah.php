<!-- Form Elements -->
                    <div class="panel panel-default">
                           <div class="panel-heading">
                                 Daftar Tanah Tersimpan
                            </div>
                             <div class="panel-body">
                                      <div class="table-responsive">
                                            <table class="table table-striped table-bordered table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>No.</th>
                                                        <th>Nama</th>
                                                       
                                                         <th>Edit/Detail</th>
                                                         <th>Hapus</th>
                                                    </tr>
                                                </thead>
                                                <tbody>   
                                                    <?php $hitung=0; foreach ($data_tanah as $tanah) { ?>
                                                    <tr>
                                                        <td><?=++$hitung;?></td>
                                                        <td><?= $tanah->nama ?></td>
                                                      
                                                        <td><a href="javascript:void(0);" onclick="edit_detail(<?= $tanah->id ?>)">Edit/Detail</a></td>
                                                        <td><a href="javascript:void(0);" onclick="hapus(<?= $tanah->id ?>)">Hapus</a></td>
                                                    </tr>
                                                    <?php } ?>
                                                </tbody>
                                            </table>
                                      </div>   
                             </div>     
                   </div>

<script>      
          //fungsi untuk menghapus data
          function hapus(id_tanah){
                var r = confirm("Apakah anda yakin ingin menghapus data ini?");
                if (r === true) {
                         //tampilkan overlay loading
                        $("#page-wrapper").LoadingOverlay("show", {
                            color   : "rgba(128,128,128, 0.8)"
                         });  
                          $.ajax({
                            url : '<?= base_url(); ?>index.php/AjaxDbTanah/hapus_tanah',
                            //mengirimkan data id ke edit_tanah
                            data : {
                                id: id_tanah
                            }, 
                            //Method pengiriman
                            type : 'POST',
                            //Data yang akan diambil dari script pemroses
                            dataType: 'html',
                            //Respon jika data berhasil dikirim
                            success : function(pesan){
                                  //sembunyikan overlay loading
                                     $("#page-wrapper").LoadingOverlay("hide");    
                                    if(pesan === 'sukses'){
                                             alert('Data berhasil dihapus');
                                             ajax('daftar_tanah', 'AjaxTanah/daftar_tanah');
                                    }else{
                                        alert(pesan);
                                    }
                            }
                        });
                }
          }
    
          //fungsi untuk mengedit dan melihat detail berdasarkan idi tanah yang dipilih
          function edit_detail(id_tanah){
              //set aktif dan nonaktif navigasi menu
              navigasi_aktif.classList.remove('active-menu');
              navigasi_aktif = document.getElementById('edit_tanah');
              navigasi_aktif.classList.add('active-menu');
              //tampilkan overlay loading
              $("#page-wrapper").LoadingOverlay("show", {
                  color   : "rgba(128,128,128, 0.8)"
               });  
                $.ajax({
                  url : '<?= base_url(); ?>index.php/AjaxTanah/edit_tanah',
                  //mengirimkan data id ke edit_tanah
                  data : {
                      id: id_tanah
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
</script>
