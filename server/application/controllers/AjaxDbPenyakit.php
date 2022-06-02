<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class AjaxDbPenyakit extends CI_Controller {
    
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
    
    public function hapus_penyakit(){
         $id = $this->input->post('id');
        if(is_null($id)){
            echo 'Gagal : variabel yang dibutuhkan bernilai null!';
        }else{
            $this->load->model('ModelPenyakit');
            $data_tanah_id = $this->ModelPenyakit->baca_penyakit($id);
            if(count($data_tanah_id)<1){
                echo "Gagal : tak dapat menghapus penyakit yang dipilih karena tanah tersebut tidak berada dalam database!";
            }else{
                //temp untuk menyimpan semua nama foto penyakit sebelum dihapus dari database
                $this->load->model('ModelFotoPenyakit');
                $arr_foto_penyakit = $this->ModelFotoPenyakit->baca_foto_penyakit($id);
                
                if($this->ModelPenyakit->hapus_penyakit($id)){
                    
                    //hapus foto tanah dari server
                     for($i=0; $i<count($arr_foto_penyakit); $i++){ 
                            if(file_exists($file=FCPATH.'/assets/upload/foto-penyakit/'.$arr_foto_penyakit[$i]->nama_file)){
                                   unlink($file);
                             }       
                     }
                    
                     //tampilkan pesan sukses
                    echo 'sukses';
                }else{
                    echo 'Gagal : Terjadi kesalahan saat menghapus data penyakit!';
                }
            }
        }
    }
    
    public function edit_ke_penyakit(){
        //area wajib
        $id = $this->input->post('id');
        $nama = $this->input->post('nama');
        $deskripsi = $this->input->post('deskripsi');
        $ciri = $this->input->post('ciri');
        $cara = $this->input->post('cara');
        $id_tanaman = $this->input->post('tanaman');
        
        //area opsional
        $foto_terhapus = $this->input->post('foto_terhapus');
        $foto_baru = $this->input->post('foto_baru');
        
        if(
                is_null($id) ||
                is_null($nama) ||
                is_null($deskripsi) ||
                is_null($ciri) ||
                is_null($cara) ||
                is_null($id_tanaman)
           ){
            echo 'Gagal : variabel yang diperlukan kosong!';
        }else{
            $this->load->model('ModelFotoPenyakit');
            $foto_penyakit = $this->ModelFotoPenyakit->baca_foto_penyakit($id);
            
            //memastikan bahwa minimal harus tersisa satu foto sebelum mengedit foto penyakit
            if(count($foto_terhapus) >= count($foto_penyakit)) {
                 echo "Gagal : Tidak dapat menghapus foto karena minimal harus terdapat 1 foto!";
            }  else{    
                $this->load->model('ModelPenyakit');
                $data_penyakit = array(
                    'id' => $id,
                    'id_tanaman' => $id_tanaman,
                    'nama' => $nama,
                    'deskripsi' => $deskripsi,
                    'ciri_ciri' => $ciri,
                    'cara_menangani' => $cara
                );
                if($this->ModelPenyakit->edit_penyakit($id, $data_penyakit)){
                        $sukses = true;
                        if(!is_null($foto_terhapus) ){
                            $arr_nama_file = array();
                            for($i=0; $i<count($foto_terhapus); $i++){
                                $temp = $this->ModelFotoPenyakit->baca_foto_penyakit_by_id($foto_terhapus[$i]);
                                $arr_nama_file[$i] = $temp[0]->nama_file;
                            }
                            if($this->ModelFotoPenyakit->hapus_foto_penyakit($foto_terhapus)){
                                //hapus file dari sisi server
                                 for($i=0; $i<count($arr_nama_file); $i++){ 
                                     if(file_exists($file=FCPATH.'/assets/upload/foto-penyakit/'.$arr_nama_file[$i])){
                                            unlink($file);
                                       }       
                                }
                            }else{
                                $sukses = false;
                                echo "Gagal : Terjadi kesalahan saat mengahapus foto!\n";
                            }
                        } 
                         if(!is_null($foto_baru)){
                            $foto_penyakit_temp = array();
                            for($i=0; $i<count($foto_baru); $i++){
                                 $foto_penyakit_temp[$i] = array( 
                                     'id_penyakit' =>  $id,
                                     'nama_file' =>  $foto_baru[$i]
                                  );
                             }
                              if(!$this->ModelFotoPenyakit->insert_ke_foto_penyakit($foto_penyakit_temp)){
                                     $sukses = false;
                                     echo 'Gagal : Terjadi kesalahan saat menyimpan foto baru!';
                               }      
                        }
                        if($sukses){
                             echo "sukses";
                        }
                }else{
                    echo 'Gagal : Terjadi kesalahan saat mengedit data penyakit!';
                }
            }
        }
    }
    
     public function insert_ke_penyakit(){
        $tanaman = $this->input->post('tanaman');
        $nama =  $this->input->post('nama');
        $deskripsi =  $this->input->post('deskripsi');
        $ciri = $this->input->post('ciri');
        $cara = $this->input->post('cara');
        $foto = $this->input->post('foto');
        if(is_null($nama) || is_null($deskripsi) || is_null($ciri) || is_null($cara) || is_null($tanaman) || is_null($foto)){
             echo "gagal";
        }else{
                $this->load->model('ModelPenyakit');
                $data_penyakit = array( 
                    'id_tanaman' =>  $tanaman,
                    'nama' =>  $nama,
                    'deskripsi' => $deskripsi,
                    'ciri_ciri' => $ciri,
                    'cara_menangani' => $cara
                );
                if($this->ModelPenyakit->insert_ke_penyakit($data_penyakit)){
                        $this->load->model('ModelFotoPenyakit');
                        $id = $this->db->insert_id();
                        $foto_penyakit = array();

                        for($i=0; $i<count($foto); $i++){
                            $foto_penyakit[$i] = array( 
                                'id_penyakit' =>  $id,
                                'nama_file' =>  $foto[$i]
                             );
                        }
                        if($this->ModelFotoPenyakit->insert_ke_foto_penyakit($foto_penyakit)){
                                  echo 'sukses';
                              }else{
                                  echo 'gagal';
                              }
                }else{
                    echo "gagal";
                }
        }
    }
    
}