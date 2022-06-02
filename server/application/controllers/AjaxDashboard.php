<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class AjaxDashboard extends CI_Controller {
    
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
    
    public function dashboard(){
       $data['real_name'] = $this->session->userdata['name'];
       $this->load->model('ModelPenyakit');
       $data['jumlah_penyakit'] = $this->ModelPenyakit->baca_jumlah_penyakit()->jumlah;
       $this->load->model('ModelTanaman');
       $data['jumlah_tanaman'] = $this->ModelTanaman->baca_jumlah_tanaman()->jumlah;
       $this->load->model('ModelTanah');
       $data['jumlah_tanah'] = $this->ModelTanah->baca_jumlah_tanah()->jumlah;
       $this->load->view('ViewDashboard', $data);
    }
    
}