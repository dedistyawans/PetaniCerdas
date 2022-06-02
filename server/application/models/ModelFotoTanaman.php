<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class ModelFotoTanaman extends CI_Model{
   
   public function insert_ke_foto_tanaman($data_foto_tanaman){
       if($this-> db->insert_batch('foto_tanaman', $data_foto_tanaman)){
               return true;
       }
      return false;
    }
    
    public function baca_semua_foto_tanaman(){
        $query = "SELECT * "
                . "FROM foto_tanaman";
        return $this->db->query($query)->result();
    }
    
    public function hapus_foto_tanaman($arr_id){
         $this->db->where_in('id', $arr_id);
         if($this->db->delete('foto_tanaman')){
             return true;
         }
         return false;
    }
    
    public function baca_foto_tanaman($id){
          $query = "SELECT * "
                . "FROM foto_tanaman "
                . "WHERE id_tanaman=$id";
         return $this->db->query($query)->result();
    }
    
    public function baca_foto_tanaman_by_id($id){
          $query = "SELECT * "
                . "FROM foto_tanaman "
                . "WHERE id=$id";
         return $this->db->query($query)->result();
    }
    
}