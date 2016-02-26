package org.siny.validator

import com.github.chengpohi.model.User

/**
 * BookMark model
 * Created by com.github.chengpohi on 7/29/15.
 */
trait UserValidator {
  def valid(user: User): Boolean
}
