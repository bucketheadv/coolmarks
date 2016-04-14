package com.github.chengpohi.validator

import com.github.chengpohi.model.User


/**
 * BookMark model
 * Created by com.github.chengpohi on 7/29/15.
 */
class LoginValidator extends UserValidator{
  override def valid(user: User): Boolean = {
    false
  }
}
