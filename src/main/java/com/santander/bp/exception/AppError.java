package com.santander.bp.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppError {
  MALFORMED_REQUEST(
      0, BAD_REQUEST, "Request has errors", "Incomplete request or type errors", null),

  CANNOT_CONNECT_TO_ELIGIBILITY_DB(
      1, INTERNAL_SERVER_ERROR, "Database issue", "Cannot connect to eligibility database", null),

  CUSTOMER_NOT_ELIGIBLE(
      100,
      UNPROCESSABLE_ENTITY,
      "Customer is not eligible",
      "Customer is not present in the eligibility list",
      null),

  CANNOT_GET_PENSION_INVESTMENTS(
      101,
      UNPROCESSABLE_ENTITY,
      "Pension investments",
      "Cannot get pension investments from PDI",
      null),

  CANNOT_GET_BLOCKED_BALANCE(
      102, UNPROCESSABLE_ENTITY, "", "Cannot get pension investments from BP", null),

  CANNOT_BLOCK_INVESTMENTS(
      103,
      UNPROCESSABLE_ENTITY,
      "Error blocking investments",
      "Cannot block pension investments from BP",
      null),

  CANNOT_GET_CUSTOMER_CODE(
      104, BAD_REQUEST, "Error getting customer code", "Cannot get customer code from BP", null),

  CANNOT_BLOCK_ALL_FUNDS(
      105,
      UNPROCESSABLE_ENTITY,
      "Error blocking one or more funds",
      "Unable to block all funds",
      null),

  CANNOT_GET_BP_FUNDS(
      106,
      UNPROCESSABLE_ENTITY,
      "Error unblocking one or more funds",
      "The customer does not have unlocking data or it was not possible to obtain all the data",
      null),

  CANNOT_GET_DRAFT(
      107,
      UNPROCESSABLE_ENTITY,
      "Error getting data from Redis",
      "An error occurred while getting data from Redis",
      null),

  CANNOT_TRIGGER_TRANSACTION(
      108,
      UNPROCESSABLE_ENTITY,
      "Error triggering transaction",
      "An error occurred when triggering the transaction in BP",
      null),

  CANNOT_CALL_PDI(
      109,
      UNPROCESSABLE_ENTITY,
      "Error triggering transaction",
      "An error occurred when triggering the transaction in BP",
      null),

  CANNOT_BLOCK_ALL_FUNDS_BP(
      110,
      UNPROCESSABLE_ENTITY,
      "Error blocking one or more funds in BP",
      "Unable to block all funds in BP",
      null),

  CANNOT_UNBLOCK_ALL_FUNDS(
      111,
      UNPROCESSABLE_ENTITY,
      "Error unblocking one or more funds",
      "Unable to unblock all funds",
      null),

  CANNOT_BLOCK_FUNDS(
      112, UNPROCESSABLE_ENTITY, "Error blocking funds", "Unable to block funds", null),

  CANNOT_GET_MF(
      100, UNPROCESSABLE_ENTITY, "Error getting transaction", "Unable to query on mainframe", null),

  CANNOT_MAPPER_TRANSACTION(
      102, INTERNAL_SERVER_ERROR, "Error convert mapper", "Unable to mapper on transaction", null),

  CANNOT_SEND_MENSSAGE(
      103, INTERNAL_SERVER_ERROR, "Error send message", "Unable to send message", null),

  ALTAR_ERROR(
      113,
      INTERNAL_SERVER_ERROR,
      "Altair Communication Error",
      "An error occurred while communicating with Altair",
      null),

  NOT_WHITELISTED(113, INTERNAL_SERVER_ERROR, "White LIST", " ERROR ", null),
  ;

  private final int code;

  private final HttpStatus httpStatus;

  private final String title;

  private final String message;

  private final Object details;

  AppError(int code, HttpStatus httpStatus, String title, String message, Object details) {
    this.code = code;
    this.httpStatus = httpStatus;
    this.title = title;
    this.message = message;
    this.details = details;
  }
}
