package v20.user;

import com.google.gson.annotations.SerializedName;
import com.oanda.v20.user.UserInfo;

/**
 * UserGetInfoResponse
 */
public class UserGetInfoResponse {

    /**
     * UserGetInfoResponse Constructor
     * <p>
     * Construct a new UserGetInfoResponse
     */
    private UserGetInfoResponse() {
    }

    @SerializedName("userInfo") private UserInfo userInfo;

    /**
     * Get the userInfo
     * <p>
     * The user information for the specifier user.
     * <p>
     * @return the userInfo
     * @see UserInfo
     */
    public UserInfo getUserInfo() {
        return this.userInfo;
    }
}
