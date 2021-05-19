package com.math.bifurcation.data.user;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Leonid Cheremshantsev
 */

@Builder
public class User implements Serializable {

    @Getter
    private final Long id;
    @Getter
    private final String username;
    @Getter
    private final Date create_ts;

    public String getChatId() {
        return String.valueOf(id);
    }

    public boolean equalsById(String id) {
        return getChatId().equals(id);
    }

    public boolean equalsById(Long id) {
        return this.id.equals(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
