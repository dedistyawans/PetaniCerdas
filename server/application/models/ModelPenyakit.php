<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class ModelPenyakit extends CI_Model{
   
    public function insert_ke_penyakit($data_penyakit){
        if($this->db->insert('penyakit', $data_penyakit)){
           return true;
       }
      return false;
    }
    
    public function edit_penyakit($id, $data_penyakit){
        $this->db->where("id", $id);
       if($this->db->update("penyakit", $data_penyakit)){
           return true;
       }   
       return false;
    }
    
    public function hapus_penyakit($id){
        $this->db->where('id', $id);
        if($this->db->delete('penyakit')){
            return true;
        }
        return false;
    }
    
    public function baca_penyakit($id){
        $query = "SELECT * "
                . "FROM penyakit "
                . "WHERE id=$id";
        return $this->db->query($query)->result();
    }
    
    public function baca_semua_penyakit(){
        $query = "SELECT * "
                . "FROM penyakit";
        return $this->db->query($query)->result();
    }
    
    public function baca_jumlah_penyakit(){
         $query = "SELECT count(*) as jumlah "
                . "FROM penyakit";
        return $this->db->query($query)->result()[0];
    }
    
    public function baca_semua_penyakit_join_tanaman(){
        $query = "SELECT p.id, p.nama , p.deskripsi, p.ciri_ciri, p.cara_menangani, t.id as id_tanaman "
                . "FROM penyakit p "
                . "JOIN tanaman t "
                . "ON p.id_tanaman = t.id";
        return $this->db->query($query)->result();
    }
    
}