<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class AjaxTanah extends CI_Controller {
    
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
    
     public function tambah_tanah(){
         $this->load->view('ViewTambahTanah');
    }
    
     public function edit_tanah(){
        $this->load->model('ModelTanah');
        $this->load->model('ModelFotoTanah');
        
        $data['aksi'] = 'edit_pilih';
        $id = $this->input->post('id');      
         if(!is_null($id)){
            $data['aksi'] = 'edit_ditentukan';
            $data['id_tanah'] = $id;
         }
        
        $data['semua_tanah'] = $this->ModelTanah->baca_semua_tanah();
        $arr_semua_foto_tanah = array();
        $semua_tanah_temp = $data['semua_tanah'];
        for($i=0; $i<count($semua_tanah_temp); $i++){
            $foto_tanah_tempt =  $this->ModelFotoTanah->baca_foto_tanah($semua_tanah_temp[$i]->id);
            for($j=0; $j<count($foto_tanah_tempt); $j++){
                $arr_semua_foto_tanah[$i][0][$j] = $foto_tanah_tempt[$j]->id;
                $arr_semua_foto_tanah[$i][1][$j] = $foto_tanah_tempt[$j]->nama_file;
            }
        }
        $data['semua_foto_tanah'] = $arr_semua_foto_tanah;
        
        $this->load->view('ViewEditTanah', $data);
    }
    
    public function daftar_tanah(){
        $this->load->model('ModelTanah');
        $data['data_tanah'] = $this->ModelTanah->baca_semua_tanah();
        $this->load->view('ViewDaftarTanah', $data);
    }
    
}