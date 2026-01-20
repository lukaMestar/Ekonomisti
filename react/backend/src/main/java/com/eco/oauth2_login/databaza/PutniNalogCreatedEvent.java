package com.eco.oauth2_login.databaza;

public class PutniNalogCreatedEvent {

   private final PutniNalog putniNalog;

   public PutniNalogCreatedEvent(PutniNalog putniNalog) {
      this.putniNalog = putniNalog;
   }

   public PutniNalog getPutniNalog() {
      return putniNalog;
   }
}
