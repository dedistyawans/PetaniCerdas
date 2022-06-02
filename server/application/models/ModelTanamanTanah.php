<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class ModelTanamanTanah extends CI_Model{
   
   public function insert_ke_tanaman_tanah($data_tanaman_tanah){
       if($this-> db->insert_batch('tanaman_tanah', $data_tanaman_tanah)){
               return true;
       }
      return false;
    }
    
    public function baca_semua_tanaman_tanah(){
        $query = "SELECT * "
                . "FROM tanaman_tanah";
         return $this->db->query($query)->result();
    }

    public function baca_tanaman_tanah_by_id($id){
          $query = "SELECT * "
                . "FROM tanaman_tanah "
                . "WHERE id=$id";
        return $this->db->query($query)->result();
    }

    public function baca_tanaman_tanah($id_tanaman){
        $query = "SELECT * "
                . "FROM tanaman_tanah "
                . "WHERE id_tanaman=$id_tanaman";
        return $this->db->query($query)->result();
    }
    
    public function hapus_semua_tanaman_tanah($arr_id){
         $this->db->where_in('id', $arr_id);
         if($this->db->delete('tanaman_tanah')){
             return true;
         }
         return false;
    }
    
}