<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class AjaxTanaman extends CI_Controller {
    
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
    
    public function daftar_tanaman(){
        $this->load->model('ModelTanaman');
        $data['data_tanaman'] = $this->ModelTanaman->baca_semua_tanaman();
        $this->load->view('ViewDaftarTanaman', $data);
    }
    
     public function tambah_tanaman(){
        $this->load->model('ModelTanah');
        $data['tanah'] = $this->ModelTanah->baca_semua_tanah();
        $this->load->view('ViewTambahTanaman', $data);
    }
    
    public function edit_tanaman(){
        $this->load->model('ModelTanaman');
        $this->load->model('ModelFotoTanaman');
        $this->load->model('ModelTanah');
        $this->load->model('ModelTanamanTanah');
        
        $data['aksi'] = 'edit_pilih';
        $id = $this->input->post('id');      
         if(!is_null($id)){
            $data['aksi'] = 'edit_ditentukan';
            $data['id_tanaman'] = $id;
         }
        
         $data['semua_tanaman'] = $this->ModelTanaman->baca_semua_tanaman();
         $semua_tanaman_temp = $data['semua_tanaman'];
         
         $arr_semua_foto_tanaman = array();
         for($i=0; $i<count($semua_tanaman_temp); $i++){
             $foto_tanaman_tempt =  $this->ModelFotoTanaman->baca_foto_tanaman($semua_tanaman_temp[$i]->id);
             for($j=0; $j<count($foto_tanaman_tempt); $j++){
                 $arr_semua_foto_tanaman[$i][0][$j] = $foto_tanaman_tempt[$j]->id;
                 $arr_semua_foto_tanaman[$i][1][$j] = $foto_tanaman_tempt[$j]->nama_file;
             }
         }
        
         $arr_tanah_terpilih = array();
         for($i=0; $i<count($semua_tanaman_temp); $i++){
             $arr_tanah_terpilih[$i] = $this->ModelTanamanTanah->baca_tanaman_tanah($semua_tanaman_temp[$i]->id);
         }
         
         $data['tanah'] = $this->ModelTanah->baca_semua_tanah();
         $data['tanah_terpilih'] = $arr_tanah_terpilih;
         
         $data['semua_foto_tanaman'] = $arr_semua_foto_tanaman;
       
         $this->load->view('ViewEditTanaman', $data);
    }
    
}