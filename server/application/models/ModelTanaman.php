<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class ModelTanaman extends CI_Model{
   
    public function insert_ke_tanaman($data_tanaman){
       if($this-> db->insert('tanaman', $data_tanaman)){
           return true;
       }
      return false;
    }
    
    public function hapus_tanaman($id){
        $this->db->where('id', $id);
        if($this->db->delete('tanaman')){
            return true;
        }
        return false;
    }
    
    public function baca_semua_tanaman(){
          $query = "SELECT * "
                . "FROM tanaman";
          return $this->db->query($query)->result();
    }
    
    public function baca_jumlah_tanaman(){
         $query = "SELECT count(*) as jumlah "
                . "FROM tanaman";
          return $this->db->query($query)->result()[0];
    }
    
    public function baca_tanaman($id){
         $query = "SELECT * "
                . "FROM tanaman "
                . "WHERE id=$id";
          return $this->db->query($query)->result()[0];
    }
    
    public function baca_tanaman_by_id($id){
          $query = "SELECT * "
                . "FROM tanaman "
                . "WHERE id=$id";
          return $this->db->query($query)->result();
    }
    
    public function edit_tanaman($id, $data_tanaman){
         $this->db->where("id", $id);
         if($this->db->update("tanaman", $data_tanaman)){
             return true;
         }   
         return false;
    }
    
}