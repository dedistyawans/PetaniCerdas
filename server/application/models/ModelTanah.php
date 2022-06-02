<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class ModelTanah extends CI_Model{
   
    public function insert_ke_tanah($data_tanah){
       if($this-> db->insert('tanah', $data_tanah)){
           return true;
       }
      return false;
    }
    
    public function hapus_tanah($id){
        $this->db->where('id', $id);
        if($this->db->delete('tanah')){
            return true;
        }
        return false;
    }
    
    public function baca_semua_tanah(){
        $query = "SELECT * "
                . "FROM tanah";
          return $this->db->query($query)->result();
    }
    
    public function baca_jumlah_tanah(){
        $query = "SELECT count(*) as jumlah "
                . "FROM tanah";
          return $this->db->query($query)->result()[0];
    }
    
    public function baca_tanah($id){
        $query = "SELECT * "
                . "FROM tanah "
                . "WHERE id=$id";
         return $this->db->query($query)->result();
    } 
    
    public function edit_tanah($id, $data_tanah){
       $this->db->where("id", $id);
       if($this->db->update("tanah", $data_tanah)){
           return true;
       }   
       return false;
    }
    
}