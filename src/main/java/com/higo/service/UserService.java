package com.higo.service;

import com.higo.exception.ImoocMallException;
import com.higo.model.pojo.User;

/**
 * Created by Intellij IDEA.
 *
 * @author ACwin YANG
 * @date 2022/05/20
 */

public interface UserService {

    User getUser(Integer id);

    void register(String userName, String password) throws ImoocMallException;

    User login(String userName, String password) throws ImoocMallException;

    void updateInformation(User user) throws ImoocMallException;

    boolean checkAdminRole(User user);
}

