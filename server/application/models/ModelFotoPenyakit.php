<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class ModelFotoPenyakit extends CI_Model{
   
    public function insert_ke_foto_penyakit($data_foto_penyakit){
       if($this-> db->insert_batch('foto_penyakit', $data_foto_penyakit)){
               return true;
       }
      return false;
    }
    
    public function baca_semua_foto_penyakit(){
        $query = "SELECT * "
                . "FROM foto_penyakit";
        return $this->db->query($query)->result();
    }
    
    public function hapus_foto_penyakit($arr_id){
         $this->db->where_in('id', $arr_id);
         if($this->db->delete('foto_penyakit')){
             return true;
         }
         return false;
    }
    
    public function baca_foto_penyakit_by_id($id){
        $query = " SELECT * "
                . "FROM foto_penyakit "
                . "WHERE id=$id";
        return $this->db->query($query)->result();
    }
    
    public function baca_foto_penyakit($id){
        $query = "SELECT * "
                . "FROM foto_penyakit "
                . "WHERE id_penyakit=$id";
        return $this->db->query($query)->result();
    }
    
}