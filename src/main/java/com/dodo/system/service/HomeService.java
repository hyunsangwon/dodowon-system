package com.dodo.system.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dodo.system.domain.CaptchaSettings;
import com.dodo.system.domain.EmpPrincipal;
import com.dodo.system.mapper.EmpMapper;
import com.dodo.system.mapper.RoleMapper;
import com.dodo.system.vo.EmpRoleVO;
import com.dodo.system.vo.EmpVO;
import com.dodo.system.vo.RoleVO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Service
public class HomeService implements UserDetailsService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CaptchaSettings captchaSettings;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public int saveEmp(EmpVO empVO) throws Exception{
        /*emp table insert*/
        empVO.setPassword(bCryptPasswordEncoder.encode(empVO.getPassword()));
        int flag = empMapper.setEmp(empVO);
        
        /*emp_role table insert*/
        RoleVO roleVO = roleMapper.getRoleInfo(empVO.getRole_name());
        EmpRoleVO empRoleVO = new EmpRoleVO();
        empRoleVO.setRole_id(roleVO.getNo());
        empVO = empMapper.findByEmpId(empVO.getId());
        empRoleVO.setEmp_id(empVO.getNo());
        flag = roleMapper.setEmpRole(empRoleVO);
        return flag;
    }

    public boolean checkEmp (EmpVO empVO) throws Exception{
        /*가입된 회원인지 체크 */
        empVO = empMapper.findByEmpId(empVO.getId());
        if(empVO == null){
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String empId) throws UsernameNotFoundException {
        EmpVO empVO = empMapper.findByEmpId(empId);
        RoleVO roleVO = roleMapper.findByEmpNo(empVO.getNo());

        if(empVO == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return new EmpPrincipal(empVO,roleVO);
    }

    public boolean verifyRecaptcha(String recaptcha) {
      	
    	final String SECRET_KEY = captchaSettings.getSecret();
    	final String RE_URL = captchaSettings.getUrl();
    	
		try {
			URL obj = new URL(RE_URL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			String postParams = "secret=" + SECRET_KEY + "&response=" + recaptcha;
			con.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
			JsonObject jsonObject = jsonReader.readObject();
			jsonReader.close();
			return jsonObject.getBoolean("success"); //true or false
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
    
    
}
