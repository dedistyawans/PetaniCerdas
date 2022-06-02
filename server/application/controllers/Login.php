<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

defined('BASEPATH') OR exit('No direct script access allowed');

class Login extends CI_Controller {

          public function __construct() {
              parent::__construct();
          }
    
        public function index() {
            if (isset($this->session->userdata['username']) && isset($this->session->userdata['name']) && isset($this->session->userdata['image'])) {
                   redirect(base_url());
             }else{
                   $this->load->view('ViewLogin'); 
             }
        }
        
        public function checklogin(){
                $username = $this->input->post('username');
                $password = $this->input->post('password');
                if(is_null($username) || is_null($password)){
                    echo "gagal";
                }else{
                     $this->load->model('ModelLogin');
                     $dbget = $this->ModelLogin->getStatusLogin($username, $password);
                     if($dbget->num_rows()>0){
                             $data_session = array(
                                 'username' => $username,
                                 'name' => $dbget->result()[0]->nama,
                                 'image' => $dbget->result()[0]->foto
                             );
                             //atur session codeigniter
	         $this->session->set_userdata($data_session);
                            echo "sukses";
                    }else{
                             echo "gagal";
                    }
                }
        }
        
        public function logout(){
                $this->session->sess_destroy();
                redirect('login');
        }

}


