<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class AjaxDbTanah extends CI_Controller {
    
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
    
    public function insert_ke_tanah(){
         $nama = $this->input->post('nama');
         $deskripsi = $this->input->post('deskripsi');
         $data_foto_tanah = $this->input->post('foto');
         
           if(is_null($nama) || is_null($deskripsi) || is_null($data_foto_tanah)){
                   echo "gagal";
          }else{   
                   $this->load->model('ModelTanah');
                   $data_tanah = array( 
                        'nama' =>  $nama,
                        'deskripsi' =>  $deskripsi
                   );
                    if($this->ModelTanah->insert_ke_tanah($data_tanah)){
                            $this->load->model('ModelFotoTanah');
                            $id = $this->db->insert_id();
                            $foto_tanah = array();
                            
                            for($i=0; $i<count($data_foto_tanah); $i++){
                                $foto_tanah[$i] = array( 
                                    'id_tanah' =>  $id,
                                    'nama_file' =>  $data_foto_tanah[$i]
                                 );
                            }
                              if($this->ModelFotoTanah->insert_ke_foto_tanah($foto_tanah)){
                                     echo 'sukses';
                               }else{
                                   echo 'gagal';
                               }
                           
                    }else{
                            echo "gagal";
                    }
          } 
    }
    
    public function edit_ke_tanah(){
        $id = $this->input->post('id');
        $nama = $this->input->post('nama');
        $deskripsi = $this->input->post('deskripsi');
        $foto_terhapus = $this->input->post('foto_terhapus');
        $foto_baru = $this->input->post('foto_baru');
        if(is_null($id) || is_null($nama) || is_null($deskripsi) ){
            echo "Gagal : Salah satu variabel yang dibutuhkan bernilai null!";
        }
        else{
                $this->load->model('ModelFotoTanah');
                $foto_tanah = $this->ModelFotoTanah->baca_foto_tanah($id);
                if(count($foto_terhapus) >= count($foto_tanah)) {
                     echo "Gagal : Tidak dapat menghapus foto karena minimal harus terdapat satu foto!";
                }  else{    
                    $this->load->model('ModelTanah');
                    $data_tanah = array( 
                         'nama' =>  $nama,
                         'deskripsi' =>  $deskripsi
                    );
                    if($this->ModelTanah->edit_tanah($id, $data_tanah)){
                        $sukses = true;
                        if(!is_null($foto_terhapus) ){
                            $arr_nama_file = array();
                            for($i=0; $i<count($foto_terhapus); $i++){
                                $temp = $this->ModelFotoTanah->baca_foto_tanah_by_id($foto_terhapus[$i]);
                                $arr_nama_file[$i] = $temp[0]->nama_file;
                            }
                            if($this->ModelFotoTanah->hapus_foto_tanah($foto_terhapus)){
                                 for($i=0; $i<count($arr_nama_file); $i++){ 
                                     if(file_exists($file=FCPATH.'/assets/upload/foto-tanah/'.$arr_nama_file[$i])){
                                            unlink($file);
                                       }       
                                }
                            }else{
                                $sukses = false;
                                echo "Gagal : Terjadi kesalahan saat mengahapus foto!\n";
                            }
                        } 
                         if(!is_null($foto_baru)){
                            $foto_tanah = array();
                            for($i=0; $i<count($foto_baru); $i++){
                                 $foto_tanah[$i] = array( 
                                     'id_tanah' =>  $id,
                                     'nama_file' =>  $foto_baru[$i]
                                  );
                             }
                              if(!$this->ModelFotoTanah->insert_ke_foto_tanah($foto_tanah)){
                                     $sukses = false;
                                     echo 'Gagal : Terjadi kesalahan saat menyimpan foto baru!';
                               }      
                        }
                        if($sukses){
                             echo "sukses";
                        }
                    }else{
                        echo "Gagal : Terjadi kegagalan saat mengedit data pada tabel tanah!";
                    }
                }             
       }
    }
    
    public function hapus_tanah(){
        $id = $this->input->post('id');
        if(is_null($id)){
            echo 'Gagal : variabel yang dibutuhkan bernilai null!';
        }else{
            $this->load->model('ModelTanah');
            $data_tanah_id = $this->ModelTanah->baca_tanah($id);
            if(count($data_tanah_id)<1){
                echo "Gagal : tak dapat menghapus tanah yang dipilih karena tanah tersebut tidak bverada dalam database!";
            }else{
                //temp untuk menyimpan semua nama foto tanah sebelum dihapus dari database
                $this->load->model('ModelFotoTanah');
                $arr_foto_tanah = $this->ModelFotoTanah->baca_foto_tanah($id);
                
                if($this->ModelTanah->hapus_tanah($id)){
                    
                    //hapus foto tanah dari server
                     for($i=0; $i<count($arr_foto_tanah); $i++){ 
                            if(file_exists($file=FCPATH.'/assets/upload/foto-tanah/'.$arr_foto_tanah[$i]->nama_file)){
                                   unlink($file);
                             }       
                     }
                    
                     //tampilkan pesan sukses
                    echo 'sukses';
                }else{
                    echo 'Gagal : Terjadi kesalahan saat menghapus data tanah!';
                }
            }
        }
    }
    
}