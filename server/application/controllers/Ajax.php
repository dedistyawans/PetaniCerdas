<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Ajax extends CI_Controller {
    
    public function __construct() {
            parent::__construct();
            //selalu periksa apakah user sudah login, jika belum redirect ke controller login
             if (!isset($this->session->userdata['username']) || !isset($this->session->userdata['name']) || !isset($this->session->userdata['image'])) {
                    redirect("login");
              }
      }
    
    public function index(){
       redirect(base_url());
    }
    
    //upload foto tanah
     function upload_foto_tanah(){
        $config['upload_path']   = FCPATH.'/assets/upload/foto-tanah/';
        $config['allowed_types'] = 'gif|jpg|png|ico';
        $this->load->library('upload',$config);

        if($this->upload->do_upload('userfile')){
        	$nama=$this->upload->data('file_name');
                    echo $nama;
        }
    }
    
     //Untuk menghapus foto tanah
    function remove_foto_tanah(){
        //Ambil nama foto
         $nama_file=$this->input->post('nama_file');
        if(file_exists($file=FCPATH.'/assets/upload/foto-tanah/'.$nama_file)){
                unlink($file);
                //tampilkan sebuah json kosong
                 echo "{}";      
        }        
    }           
    
        //upload foto tanaman
     function upload_foto_tanaman(){
        $config['upload_path']   = FCPATH.'/assets/upload/foto-tanaman/';
        $config['allowed_types'] = 'gif|jpg|png|ico';
        $this->load->library('upload',$config);

        if($this->upload->do_upload('userfile')){
        	$nama=$this->upload->data('file_name');
                    echo $nama;
        }
    }
    
     //Untuk menghapus foto tanaman
    function remove_foto_tanaman(){
        //Ambil nama foto
         $nama_file=$this->input->post('nama_file');
        if(file_exists($file=FCPATH.'/assets/upload/foto-tanaman/'.$nama_file)){
                unlink($file);
                //tampilkan sebuah json kosong
                 echo "{}";      
        }        
    }   
    
    function upload_foto_penyakit(){
        $config['upload_path']   = FCPATH.'/assets/upload/foto-penyakit/';
        $config['allowed_types'] = 'gif|jpg|png|ico';
        $this->load->library('upload',$config);

        if($this->upload->do_upload('userfile')){
        	$nama=$this->upload->data('file_name');
                    echo $nama;
        }
    }
    
    function remove_foto_penyakit(){
          //Ambil nama foto
         $nama_file=$this->input->post('nama_file');
        if(file_exists($file=FCPATH.'/assets/upload/foto-penyakit/'.$nama_file)){
                unlink($file);
                //tampilkan sebuah json kosong
                 echo "{}";      
        }      
    }

}