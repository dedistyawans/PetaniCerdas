<?php
defined('BASEPATH') OR exit('No direct script access allowed');
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of m_login
 *
 * @author DekaStudio
 */
class ModelLogin extends CI_Model{
    //put your code here
   
    public function getStatusLogin($username, $password){
        $query = 
                "SELECT * "
                . "FROM admin "
                . "WHERE username='$username' "
                . "AND password='$password'";
       return  $this->db->query($query);
    }
    
}
