<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Home extends CI_Controller {
          public function __construct() {
              parent::__construct();
               if (!isset($this->session->userdata['username']) || !isset($this->session->userdata['name']) || !isset($this->session->userdata['image'])) {
                      redirect("login");
                }
          }
    
        public function index() {           
            $data_header['user_name'] = $this->session->userdata['username'];
            $data_header['image'] = $this->session->userdata['image'];
            $data_content['real_name'] = $this->session->userdata['name'];
            
            $this->load->model('ModelPenyakit');
            $data_content['jumlah_penyakit'] = $this->ModelPenyakit->baca_jumlah_penyakit()->jumlah;
            
            $this->load->model('ModelTanaman');
            $data_content['jumlah_tanaman'] = $this->ModelTanaman->baca_jumlah_tanaman()->jumlah;
            
            $this->load->model('ModelTanah');
            $data_content['jumlah_tanah'] = $this->ModelTanah->baca_jumlah_tanah()->jumlah;
            
            
            $this->load->view('coreview/header', $data_header);
            $this->load->view('coreview/navigation'); 
            $this->load->view('ViewDashboard', $data_content); 
            $this->load->view('coreview/footer'); 
        }
        
        
}


