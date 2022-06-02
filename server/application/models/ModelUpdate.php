<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class ModelUpdate extends CI_Model{
     public function baca_versi_terbaru(){
          $query = "SELECT * "
                . "FROM `update` "
                . "order by versi desc "
                . "limit 1";
        return $this->db->query($query)->result()[0]->versi;
    }
    
     public function baca_semua_update($versi_client){
        $query = "SELECT * "
                . "FROM `update` "
                . "WHERE versi > $versi_client";
         return $this->db->query($query)->result();
    }
}
