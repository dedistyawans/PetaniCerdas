<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

defined('BASEPATH') OR exit('No direct script access allowed');


?>


<!doctype html>
<html lang="en-US">
<head>
    <meta charset="utf-8">
    <title>Login</title>
    
      <!-- Favicon-->
    <link rel="icon" href="<?= base_url(); ?>assets/icon/favicon.ico" type="image/x-icon">
    
    <!-- css diambil dari assets untuk form login -->
    <link rel="stylesheet" type="text/css" href="<?php echo base_url(); ?>assets/css/css_login.css">
    
    <script type="text/javascript" src="<?php echo base_url(); ?>assets/js/jquery-3.2.1.js"> </script>
    <script type="text/javascript" src="<?php echo base_url(); ?>assets/js/jqueryMD5.js"> </script>
         <script>
                function check_login(){
                        //Mengambil value dari input username & password
                        var username = $('#username').val();
                        var password = $.md5($('#password').val());
                        //var password;
                        
                        //alamat login dan home admin
                        var url_login     = '<?php echo base_url();  ?>index.php/Login/checklogin';
                        var url_admin   = '<?php echo base_url();  ?>';

                        //Ubah tulisan pada button saat click login
                        $('#btnLogin').attr('value','Memuat ...');
                        //Gunakan jquery AJAX
                        $.ajax({
                                url	: url_login,
                                //mengirimkan username dan password ke  checklogin
                                data : 'username='+username+'&password='+password, 
                                //Method pengiriman
                                type : 'POST',
                                //Data yang akan diambil dari script pemroses
                                dataType: 'html',
                                //Respon jika data berhasil dikirim
                                success : function(pesan){
                                        if(pesan==='sukses'){
                                                //Arahkan ke halaman admin jika script pemroses mencetak kata succes
                                                window.location = url_admin;
                                        }else{
                                                //Cetak peringatan untuk username & password salah
                                                alert("Username atau password salah");
                                                //alert(pesan);
                                                $('#btnLogin').attr('value','Coba Lagi');
                                        }
                                }
                        });
                }
                
        </script>
</head>
<body>
          <div id="login">
                    <h2><span class="fontawesome-lock"></span>Sign In</h2>
                    <fieldset action="#">
                            <p><label for="email">Username</label></p>
                            <p><input type="email" id="username" placeholder="username"  required></p>
                            <p><label for="password">Password</label></p>
                            <p><input type="password" id="password" placeholder="password" required></p> 
                            <p><input type="submit" value="Masuk" id="btnLogin" onclick="check_login();"></p>
                    </fieldset>
          </div> 
</body>	
</html>