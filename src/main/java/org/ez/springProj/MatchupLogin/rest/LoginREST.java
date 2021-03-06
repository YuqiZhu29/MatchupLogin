package org.ez.springProj.MatchupLogin.rest;

import org.ez.springProj.MatchupLogin.model.UserModel;
import org.ez.springProj.MatchupLogin.query.AuthenticationService;
import org.ez.springProj.MatchupLogin.query.PostgreSQLJDBCService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/authentication")
public class LoginREST {
    private final static Logger logger = LoggerFactory.getLogger(LoginREST.class);
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    PostgreSQLJDBCService postgreJDBC;

    @ApiOperation(value = "user-login", notes = "user login")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String", required = true)
    })
    @RequestMapping(value = "/user-login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<List<String>> login(@RequestParam(name = "userId") String userId,
                                              @RequestParam(name = "password") String password) {
        List<String> returnList = new ArrayList<String>();
        if(userId == null || userId == "" || password == null || password == "") {
            returnList.add("Invalid Username or Password!");
            return new ResponseEntity<>(returnList, HttpStatus.NOT_FOUND);
        }
        UserModel userInDataBase = postgreJDBC.findUser(userId);
        if (userInDataBase == null) {
            returnList.add("User Unavailable or Not Found!");
            return new ResponseEntity<>(returnList, HttpStatus.NOT_FOUND);
        } else if (!password.equals(userInDataBase.getPassWord())) {
            returnList.add("Incorrect Password!");
            return new ResponseEntity<>(returnList, HttpStatus.UNAUTHORIZED);
        } else {
            String token = "admin";
            returnList.add("token: " + token);
            returnList.add("user: " + userInDataBase.toString());
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }
}
