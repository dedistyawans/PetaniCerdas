        <body>
            <div id="wrapper">
                <nav class="navbar navbar-default navbar-cls-top " role="navigation" style="margin-bottom: 0">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="<?= base_url(); ?>"><?= $user_name ?> </a> 
                    </div>
          <div style="color: white;
        padding: 15px 50px 5px 50px;
        float: right;
        font-size: 16px;"> <a href="<?= base_url(); ?>index.php/login/logout" class="btn btn-danger square-btn-adjust">Logout</a> </div>
                </nav>   
                   <!-- /. NAV TOP  --> 
        <nav class="navbar-default navbar-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="main-menu">
	<li class="text-center">
                        <img src="<?= base_url(); ?>assets/img/<?= $image ?>" class="user-image img-responsive"/>
	</li>
                    <li>
                        <a href="javascript:void(0);" id="dashboard"> Dashboard</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);"> Tanaman<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a href="javascript:void(0);" id="daftar_tanaman">Daftar Tanaman</a>
                            </li>
                            <li>
                                <a href="javascript:void(0);" id="tambahkan_tanaman" >Tambahkan Tanaman</a>
                            </li>
                              <li>
                                <a href="javascript:void(0);" id="edit_tanaman" >Edit Tanaman</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="javascript:void(0);"> Tanah<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a  href="javascript:void(0);" id="daftar_tanah">Daftar Tanah</a>
                            </li>
                            <li>
                                <a href="javascript:void(0);"id="tambahkan_tanah" >Tambahkan Tanah</a>
                            </li>
                             <li>
                                <a href="javascript:void(0);"id="edit_tanah" >Edit Tanah</a>
                            </li>
                        </ul>
                    </li>
                   
                     <li>
                        <a href="javascript:void(0);"> Penyakit<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a  href="javascript:void(0);" id="daftar_penyakit">Daftar Penyakit</a>
                            </li>
                            <li>
                                <a href="javascript:void(0); "id="tambahkan_penyakit" >Tambahkan Penyakit</a>
                            </li>
                             <li>
                                <a href="javascript:void(0); "id="edit_penyakit" >Edit Penyakit</a>
                            </li>
                        </ul>
                    </li>
                   
                </ul>
               
            </div>
            
        </nav>  
        <!-- /. NAV SIDE  -->
        <div id="page-wrapper" >

       
        