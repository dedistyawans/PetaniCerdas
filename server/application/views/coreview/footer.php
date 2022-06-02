        </div>
     <!-- /. WRAPPER  -->
   <input type="hidden" id="base_url" value="<?php echo base_url(); ?>">
   <script>
           //base url codeigniter
        var base_url = $('#base_url').val();
        //variabel navigasi aktif
        var navigasi_aktif = document.getElementById("dashboard");
        navigasi_aktif.classList.add('active-menu');

        $('#dashboard').click(function(){
            ajax('dashboard', 'AjaxDashboard/dashboard');
        });

        $('#tambahkan_tanaman').click(function(){
            ajax('tambahkan_tanaman', 'AjaxTanaman/tambah_tanaman');
        });
        
        $('#daftar_tanaman').click(function(){
            ajax('daftar_tanaman', 'AjaxTanaman/daftar_tanaman');
        });
        
         $('#edit_tanaman').click(function(){
            ajax('edit_tanaman', 'AjaxTanaman/edit_tanaman');
        });

       $('#tambahkan_tanah').click(function(){
            ajax('tambahkan_tanah', 'AjaxTanah/tambah_tanah');
        });
        
         $('#edit_tanah').click(function(){
            ajax('edit_tanah', 'AjaxTanah/edit_tanah');
        });
        
         $('#daftar_tanah').click(function(){
            ajax('daftar_tanah', 'AjaxTanah/daftar_tanah');
        });
        
         $('#daftar_penyakit').click(function(){
            ajax('daftar_penyakit', 'AjaxPenyakit/daftar_penyakit');
        });
        
         $('#edit_penyakit').click(function(){
            ajax('edit_penyakit', 'AjaxPenyakit/edit_penyakit');
        });
        
        $('#tambahkan_penyakit').click(function(){
            ajax('tambahkan_penyakit', 'AjaxPenyakit/tambah_penyakit');
        });

        function ajax(id, alamat_ajax){
           //set aktif dan nonaktif navigasi menu
            navigasi_aktif.classList.remove('active-menu');
            navigasi_aktif = document.getElementById(id);
            navigasi_aktif.classList.add('active-menu');
             //tampilkan overlay loading
              $("#page-wrapper").LoadingOverlay("show", {
                color   : "rgba(128,128,128, 0.8)"
               });      
            //Gunakan jquery AJAX
            $.ajax({
                    //alamat dr ajax yang ingin diambil datanya
                    url : base_url+'index.php/'+alamat_ajax,
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
   
   
    <script src="<?= base_url(); ?>assets/js/bootstrap.min.js"></script>
    <!-- METISMENU SCRIPTS -->
    <script src="<?= base_url(); ?>assets/js/jquery.metisMenu.js"></script>

      <!-- CUSTOM SCRIPTS -->
    <script src="<?= base_url(); ?>assets/js/custom.js"></script>
    
   
</body>
</html>