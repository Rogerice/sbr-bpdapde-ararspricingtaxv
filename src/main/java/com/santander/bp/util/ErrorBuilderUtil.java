package com.santander.bp.util;

import com.santander.bp.model.Error;
import com.santander.bp.model.Error.LevelEnum;
import com.santander.bp.model.Errors;
import java.util.Collections;

public class ErrorBuilderUtil {

  public static Errors buildError(
      String code, String message, String description, LevelEnum level) {
    Error error =
        Error.builder().code(code).message(message).level(level).description(description).build();

    return Errors.builder().errors(Collections.singletonList(error)).build();
  }

  public static Errors buildServerError(String description) {
    return buildError("500", "Internal Server Error", description, LevelEnum.ERROR);
  }

  public static Errors buildNotFoundError(String description) {
    return buildError("NOT_FOUND", "No offers found", description, LevelEnum.INFO);
  }
}
