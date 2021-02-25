package com.program.moist.entity.relations;

import lombok.Data;

/**
 * Date: 2021/2/25
 * Author: SilentSherlock
 * Description: describe user's subscription
 */
@Data
public class Subscription {

    private int from_user_id;
    private int to_user_id;
}
