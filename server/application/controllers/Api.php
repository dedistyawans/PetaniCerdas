<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Api extends CI_Controller {
    
    public function index(){
            $this->load->model('ModelTanaman');
            $api['tanaman'] = $this->ModelTanaman->baca_semua_tanaman();
            
            $this->load->model('ModelFotoTanaman');
            $api['foto_tanaman'] = $this->ModelFotoTanaman->baca_semua_foto_tanaman();
            
            $this->load->model('ModelTanamanTanah');
            $api['tanaman_tanah'] = $this->ModelTanamanTanah->baca_semua_tanaman_tanah();
            
            $this->load->model('ModelTanah');
            $api['tanah'] = $this->ModelTanah->baca_semua_tanah();
            
            $this->load->model('ModelFotoTanah');
            $api['foto_tanah'] = $this->ModelFotoTanah->baca_semua_foto_tanah();
            
            $this->load->model('ModelPenyakit');
            $api['penyakit'] = $this->ModelPenyakit->baca_semua_penyakit();
            
            $this->load->model('ModelFotoPenyakit');
            $api['foto_penyakit'] = $this->ModelFotoPenyakit->baca_semua_foto_penyakit();
            
            $this->load->model('ModelUpdate');
            $api['versi'] = $this->ModelUpdate->baca_versi_terbaru();
            
            echo json_encode($api);
    }
    
    public function tanaman($id = null){
         $this->load->model('ModelTanaman');
         if($id == null){
                   $api = $this->ModelTanaman->baca_semua_tanaman();
                    echo json_encode($api);
         }else{
                   $api = $this->ModelTanaman->baca_tanaman_by_id($id);
                   if(count($api) <1 ){
                       $api = '{}';
                       echo $api;
                   }else{
                       $api = $api[0];
                        echo json_encode($api);
                   }
         }
    }
    
    public function tanah($id = null){
        $this->load->model('ModelTanah');
        if($id == null){
            $api = $this->ModelTanah->baca_semua_tanah();
            echo json_encode($api);
        }else{
            $api = $this->ModelTanah->baca_tanah($id);
            if(count($api) <1 ){
                $api = '{}';
                echo $api;
            }else{
                $api = $api[0];
                echo json_encode($api);
            }          
        }
        
    }
    
    public function foto_tanah($id = null){
        $this->load->model('ModelFotoTanah');
        if($id == null){
            $api = $this->ModelFotoTanah->baca_semua_foto_tanah();
            echo json_encode($api);
        }else{
            $api = $this->ModelFotoTanah->baca_foto_tanah_by_id($id);
             if(count($api) <1 ){
                $api = '{}';
                echo $api;
            }else{
                $api = $api[0];
                echo json_encode($api);
            }  
        }
    }
    
    public function foto_tanaman($id = null){
         $this->load->model('ModelFotoTanaman');
         if($id == null){
             $api = $this->ModelFotoTanaman->baca_semua_foto_tanaman();
              echo json_encode($api);
         }else{
             $api = $this->ModelFotoTanaman->baca_foto_tanaman_by_id($id);
             if(count($api) <1 ){
                 $api = '{}';
                 echo $api;
             }else{
                 $api = $api[0];
                  echo json_encode($api);
             }  
         }
    }
    
    public function penyakit($id = null){
        $this->load->model('ModelPenyakit');
        if($id == null){
            $api = $this->ModelPenyakit->baca_semua_penyakit();
             echo json_encode($api);
        }else{
            $api = $this->ModelPenyakit->baca_penyakit($id);
            if(count($api) <1 ){
                $api = '{}';
                echo $api;
            }else{
                $api = $api[0];
                 echo json_encode($api);
            }  
        }
    }
    
    public function foto_penyakit($id = null){
        $this->load->model('ModelFotoPenyakit');
        if($id == null){
            $api = $this->ModelFotoPenyakit->baca_semua_foto_penyakit();
             echo json_encode($api);
        }else{
            $api = $this->ModelFotoPenyakit->baca_foto_penyakit_by_id($id);
            if(count($api) <1 ){
                $api = '{}';
                echo $api;
            }else{
                $api = $api[0];
                 echo json_encode($api);
            }  
        }
    }
    
    public function tanaman_tanah($id = null){
         $this->load->model('ModelTanamanTanah');
         if($id==null){
             $api = $this->ModelTanamanTanah->baca_semua_tanaman_tanah();
              echo json_encode($api);
         }else{
             $api = $this->ModelTanamanTanah->baca_tanaman_tanah_by_id($id);
            if(count($api) <1 ){
                $api = '{}';
                echo $api;
            }else{
                 $api = $api[0];
                 echo json_encode($api);
            }  
         }
    }
    
    public function update($versi_client = null){
          $this->load->model('ModelUpdate');
          $versi_server =  $this->ModelUpdate->baca_versi_terbaru();
          
          if($versi_client == null){
                   $api['versi'] = $versi_server;
          } else  if($versi_client<$versi_server){
                   $api =  $this->ModelUpdate->baca_semua_update($versi_client);
          }else{
                   $api['versi'] = $versi_server;
          }
         echo json_encode($api);
    }
         
}

