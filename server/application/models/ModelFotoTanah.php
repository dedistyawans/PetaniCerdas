<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class ModelFotoTanah extends CI_Model{
   
    public function insert_ke_foto_tanah($data_foto_tanah){
       if($this-> db->insert_batch('foto_tanah', $data_foto_tanah)){
               return true;
       }
      return false;
    }
   
    public function baca_semua_foto_tanah(){
        $query = "SELECT * "
                . "FROM foto_tanah";
         return $this->db->query($query)->result();
    }
    
    public function baca_foto_tanah($id){
        $query = "SELECT * "
                . "FROM foto_tanah "
                . "WHERE id_tanah=$id";
         return $this->db->query($query)->result();
    }
    
    public function baca_foto_tanah_by_id($id){
        $query = "SELECT * "
                . "FROM foto_tanah "
                . "WHERE id=$id";
         return $this->db->query($query)->result();
    }
    
    public function hapus_foto_tanah($arr_id){
         $this->db->where_in('id', $arr_id);
         if($this->db->delete('foto_tanah')){
             return true;
         }
         return false;
    }
    
}