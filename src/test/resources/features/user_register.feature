Feature: User register

  Scenario: User registers successfully
    Given user is unknown
    When user is registered with success
    Then user is known

  Scenario: User registers with invalid email
    Given user without valid email
    When user fails to register
    Then user is still unknown

  Scenario: User login with valid password
    Given user is registered
    When user logins with valid password
    Then user gets an access token

  Scenario: User login with invalid password
    Given user is registered
    When user logins with invalid password
    Then user fails to login
