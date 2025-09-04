package ci.koodysgroup.domains.dtms;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class UserOrOtpDtm {
        private UserDtm user;
        private OtpDtm code;

        private UserOrOtpDtm() {
        }

        public static UserOrOtpDtm ofUser(UserDtm user) {
            UserOrOtpDtm instance = new UserOrOtpDtm();
            instance.user = user;
            return instance;
        }

        public static UserOrOtpDtm ofCode(OtpDtm code) {
            UserOrOtpDtm instance = new UserOrOtpDtm();
            instance.code = code;
            return instance;
        }


        // Builder pattern
        public static class Builder {
            private UserDtm user;
            private OtpDtm code;

            public Builder user(UserDtm user) {
                this.user = user;
                this.code = null;
                return this;
            }

            public Builder code(OtpDtm code) {
                this.user = null;
                this.code = code;
                return this;
            }

            public UserOrOtpDtm build() {
                UserOrOtpDtm instance = new UserOrOtpDtm();
                instance.user = this.user;
                instance.code = this.code;
                return instance;
            }
        }

        public static Builder builder() {
            return new Builder();
        }

}