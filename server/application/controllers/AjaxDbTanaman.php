<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class AjaxDbTanaman extends CI_Controller {
    
    public function __construct() {
            parent::__construct();
            //selalu periksa apakah user sudah login, jika belum redirect ke controller login
             if (!isset($this->session->userdata['username']) || !isset($this->session->userdata['name']) || !isset($this->session->userdata['image'])) {
                    redirect("login");
              }
      }
    
    public function index(){
        //direct akses ke controller ini tidak diperbolehkan, redirect ke home page
       redirect(base_url());
    }
    
    public function hapus_tanaman(){
        $id = $this->input->post('id');
        if(is_null($id)){
            echo 'Gagal : variabel yang dibutuhkan tidak terpenuhi!';
        }else{
            //load model foto tanaman dan baca semua foto tanaman dengan id ini
            $this->load->model('ModelFotoTanaman');
            $data_foto_tanaman = $this->ModelFotoTanaman->baca_foto_tanaman($id);
            
            //load model tanaman dan hapus data tanaman dari database
            $this->load->model('ModelTanaman');
            if($this->ModelTanaman->hapus_tanaman($id)){
                 
                //hapus foto tanaman dari server
                for($i=0; $i<count($data_foto_tanaman); $i++){
                      if(file_exists($file=FCPATH.'/assets/upload/foto-tanaman/'.$data_foto_tanaman[$i]->nama_file)){
                             unlink($file);
                       }       
                }
                
                //kembalikan nilai sukses
                echo 'sukses';
            }else{
                echo 'Gagal : Terjadi kesalahan saat menghapus foto tanaman!';
            }
        }
    }
    
    public function edit_ke_tanaman(){
            //variabel wajib ada
            $id =  $this->input->post('id');
            $nama =  $this->input->post('nama');
            $umur = $this->input->post('umur');
            $musim = $this->input->post('musim');
            $ketinggian_min = $this->input->post('ketinggian_min');
            $ketinggian_max = $this->input->post('ketinggian_max');
            $curah_hujan_min = $this->input->post('curah_hujan_min');
            $curah_hujan_max = $this->input->post('curah_hujan_max');
            $suhu_min = $this->input->post('suhu_min');
            $suhu_max = $this->input->post('suhu_max');
            $deskripsi = $this->input->post('deskripsi');
            $rekomendasi_menanam = $this->input->post('rekomendasi_menanam');
            
            //variabel opsional
            $foto_terhapus = $this->input->post('foto_terhapus');
            $foto_baru = $this->input->post('foto_baru');
            $data_tanah_baru = $this->input->post('tanah');
            
            if(
                    is_null($id) ||
                    is_null($nama) ||
                    is_null($umur) ||
                    is_null($musim) ||
                    is_null($ketinggian_min) ||
                    is_null($ketinggian_max) ||
                    is_null($curah_hujan_min) ||
                    is_null($curah_hujan_max) || 
                    is_null($suhu_min) || 
                    is_null($suhu_max) || 
                    is_null($deskripsi) ||
                    is_null($rekomendasi_menanam)  
              ){
                 //jika salah satu variabel post diatas bernilai null maka proses insert tidak bisa dilakukan
                echo "gagal";
            }else{
                //load model foto tanaman
                $this->load->model('ModelFotoTanaman');
                $foto_tanaman = $this->ModelFotoTanaman->baca_foto_tanaman($id);
                if(count($foto_terhapus) >= count($foto_tanaman)) {
                     echo "Gagal : Tidak dapat menghapus foto karena minimal harus terdapat satu foto!";
                } else{   
                    //load ModelTanaman
                   $this->load->model('ModelTanaman');
                   $data_tanaman = array(
                       'nama' =>  $nama,
                       'umur' =>  $umur,
                       'musim' => $musim,
                       'ketinggian_min' => $ketinggian_min,
                       'ketinggian_max' => $ketinggian_max,
                       'curah_hujan_min' => $curah_hujan_min,
                       'curah_hujan_max' => $curah_hujan_max,
                       'suhu_min' => $suhu_min,
                       'suhu_max' => $suhu_max,
                       'deskripsi' => $deskripsi,
                       'rekomendasi_menanam' => $rekomendasi_menanam
                     );
                   if($this->ModelTanaman->edit_tanaman($id, $data_tanaman)){
                           $sukses = true;
                           
                           //untuk foto yang dihapus
                           if(!is_null($foto_terhapus) ){
                               $arr_nama_file = array();
                               for($i=0; $i<count($foto_terhapus); $i++){
                                   $temp = $this->ModelFotoTanaman->baca_foto_tanaman_by_id($foto_terhapus[$i]);
                                   $arr_nama_file[$i] = $temp[0]->nama_file;
                               }
                               if($this->ModelFotoTanaman->hapus_foto_tanaman($foto_terhapus)){
                                    for($i=0; $i<count($arr_nama_file); $i++){ 
                                        //setelah dihapus dari database, hapus juga file nya dari server
                                        if(file_exists($file=FCPATH.'/assets/upload/foto-tanaman/'.$arr_nama_file[$i])){
                                               unlink($file);
                                          }       
                                   }
                               }else{
                                   $sukses = false;
                                   echo "Gagal : Terjadi kesalahan saat mengahapus foto!\n";
                               }
                           } 
                           
                           //untuk foto baru
                           if(!is_null($foto_baru)){
                            $foto_tanaman = array();
                            for($i=0; $i<count($foto_baru); $i++){
                                 $foto_tanaman[$i] = array( 
                                     'id_tanaman' =>  $id,
                                     'nama_file' =>  $foto_baru[$i]
                                  );
                             }
                              if(!$this->ModelFotoTanaman->insert_ke_foto_tanaman($foto_tanaman)){
                                     $sukses = false;
                                     echo 'Gagal : Terjadi kesalahan saat menyimpan foto baru!';
                               }      
                        }
                        
                        //untuk checkbox tanah
                        if(!is_null($data_tanah_baru)){
                            //load model dan baca semua data tanah dengan id tanaman ini
                            $this->load->model('ModelTanamanTanah');
                            $data_tanaman_tanah = $this->ModelTanamanTanah->baca_tanaman_tanah($id);
   
                             if(count($data_tanaman_tanah)>0){
                                   //menyimpan daftar id foto_tanaman yang lama
                                    $data_tanah_lama = array();
                                    for($i=0; $i<count($data_tanaman_tanah); $i++){
                                            $data_tanah_lama[$i] = $data_tanaman_tanah[$i]->id; 
                                    }
                                 
                                    //hapus data tanah untuk tanaman dengan id ini
                                    if($this->ModelTanamanTanah->hapus_semua_tanaman_tanah($data_tanah_lama)){

                                        //insert data baru ke database
                                        $data_checkbox = array();
                                        for($i=0; $i<count($data_tanah_baru); $i++){
                                            $data_checkbox[$i] = array( 
                                                'id_tanaman' =>  $id,
                                                'id_tanah' =>  $data_tanah_baru[$i]
                                             );
                                        }

                                         if(!$this->ModelTanamanTanah->insert_ke_tanaman_tanah($data_checkbox)){
                                              $sukses = false;
                                              echo 'Gagal : Terjadi kesalahan saat memperbarui data tanah untuk tanaman ini!';
                                          }
                                    }else{
                                        $sukses = false;
                                        echo 'Gagal : Terjadi kesalahan saat memperbarui data tanah untuk tanaman ini!';
                                    }
                            }else{
                                    //insert data baru ke database
                                    $data_checkbox = array();
                                    for($i=0; $i<count($data_tanah_baru); $i++){
                                        $data_checkbox[$i] = array( 
                                            'id_tanaman' =>  $id,
                                            'id_tanah' =>  $data_tanah_baru[$i]
                                         );
                                    }
                                     if(!$this->ModelTanamanTanah->insert_ke_tanaman_tanah($data_checkbox)){
                                          $sukses = false;
                                          echo 'Gagal : Terjadi kesalahan saat memperbarui data tanah untuk tanaman ini!';
                                      }
                            }
                        }
                        if($sukses){
                             echo "sukses";
                        }
                   }else{
                        echo "Gagal : Terjadi kegagalan saat mengedit data tanaman!";
                    }
                }
            }
         
    }
    
    public function insert_ke_tanaman(){
        $nama = $this->input->post('nama');
        $deskripsi = $this->input->post('deskripsi');
        $foto = $this->input->post('foto');
        $tanah = $this->input->post('tanah');
        $umur = $this->input->post('umur');
        $musim = $this->input->post('musim');
        $ketinggian_min = $this->input->post('ketinggian_min');
        $ketinggian_max = $this->input->post('ketinggian_max');
        $curah_hujan_min = $this->input->post('curah_hujan_min');
        $curah_hujan_max = $this->input->post('curah_hujan_max');
        $suhu_min = $this->input->post('suhu_min');
        $suhu_max = $this->input->post('suhu_max');
        $rekomendasi_menanam= $this->input->post('rekomendasi_menanam');
        
         if(is_null($nama) || is_null($deskripsi)  || is_null($foto)  || is_null($tanah) || is_null($umur) 
                 || is_null($musim) ||  is_null($ketinggian_min) ||  is_null($ketinggian_max)
                         || is_null($curah_hujan_min) || is_null($curah_hujan_max) || is_null($suhu_min) || is_null($suhu_max) || is_null($rekomendasi_menanam)){
                //jika salah satu variabel post diatas bernilai null maka proses insert tidak bisa dilakukan
                echo "gagal";
         //jika variabel tidak null maka aman untuk melakukan proses insert ke database
         }else{
            //load ModelTanaman
             $this->load->model('ModelTanaman');
             $data_tanaman = array(
                 'nama' =>  $nama,
                 'umur' =>  $umur,
                 'musim' => $musim,
                 'ketinggian_min' => $ketinggian_min,
                 'ketinggian_max' => $ketinggian_max,
                 'curah_hujan_min' => $curah_hujan_min,
                 'curah_hujan_max' => $curah_hujan_max,
                 'suhu_min' => $suhu_min,
                 'suhu_max' => $suhu_max,
                 'deskripsi' => $deskripsi,
                 'rekomendasi_menanam' => $rekomendasi_menanam
             );
             if($this->ModelTanaman->insert_ke_tanaman($data_tanaman)){
                 //load model
                 $this->load->model('ModelFotoTanaman');
                 $this->load->model('ModelTanamanTanah');
                 
                 //simpan variabel temp
                 $id_tanaman = $this->db->insert_id();
                 $foto_tanaman = array();
                 $data_tanah = array();
                 
                  for($i=0; $i<count($foto); $i++){
                        $foto_tanaman[$i] = array( 
                            'id_tanaman' =>  $id_tanaman,
                            'nama_file' =>  $foto[$i]
                         );
                    }
                    
                    for($i=0; $i<count($tanah); $i++){
                       $data_tanah[$i] = array( 
                           'id_tanaman' =>  $id_tanaman,
                           'id_tanah' =>  $tanah[$i]
                        );
                    }
                       
                    if($this->ModelFotoTanaman->insert_ke_foto_tanaman($foto_tanaman) 
                            && $this->ModelTanamanTanah->insert_ke_tanaman_tanah($data_tanah) ){
                                echo 'sukses';
                    }else{
                        echo 'gagal';
                    }
                   
             }
         }
    } 
    
}