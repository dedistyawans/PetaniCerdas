<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class AjaxPenyakit extends CI_Controller {
    
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
    
    public function daftar_penyakit(){
        $this->load->model('ModelPenyakit');
        $data['data_penyakit'] = $this->ModelPenyakit->baca_semua_penyakit();
        $this->load->view('ViewDaftarPenyakit', $data);
    }
    
    //fungsi ajax untuk view edit penyakit
    public function edit_penyakit(){
        
        $data['aksi'] = 'edit_pilih';
        $id = $this->input->post('id');      
         if(!is_null($id)){
            $data['aksi'] = 'edit_ditentukan';
            $data['id_penyakit'] = $id;
         }
        
        //load penyakit tanaman
        $this->load->model('ModelPenyakit');
        $data['data_penyakit'] = $this->ModelPenyakit->baca_semua_penyakit_join_tanaman();
        
        //load foto penyakit
        $this->load->model('ModelFotoPenyakit');
        
        //simpan foto penyakit
        $arr_foto_penyakit = array();
        for($i=0; $i<count($data['data_penyakit']); $i++){
            $arr_foto_penyakit[$i] = $this->ModelFotoPenyakit->baca_foto_penyakit($data['data_penyakit'][$i]->id);
        }
        
        $data['foto_penyakit'] = $arr_foto_penyakit;
        
        //load data tanaman
        $this->load->model('ModelTanaman');
        $data['data_tanaman'] = $this->ModelTanaman->baca_semua_tanaman();
        
        $this->load->view('ViewEditPenyakit', $data);
    }
    
     //fungsi ajax untuk view tambah penyakit
    public function tambah_penyakit(){
        $this->load->model('ModelTanaman');
        $data['tanaman'] = $this->ModelTanaman->baca_semua_tanaman();
        $this->load->view('ViewTambahPenyakit', $data);
    }
    
}