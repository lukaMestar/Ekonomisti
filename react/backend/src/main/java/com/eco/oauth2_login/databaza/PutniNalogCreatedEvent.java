package com.eco.oauth2_login.databaza;

import com.eco.oauth2_login.databaza.PutniNalog;

public class PutniNalogCreatedEvent {

   private final PutniNalog putniNalog;

   public PutniNalogCreatedEvent(PutniNalog putniNalog) {
      this.putniNalog = putniNalog;
   }

   public PutniNalog getPutniNalog() {
      return putniNalog;
   }
}
