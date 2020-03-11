package com.qgyyzs.globalcosmetics.uikit.business.contact.core.util;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.search.model.MsgIndexRecord;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.qgyyzs.globalcosmetics.uikit.business.contact.core.model.IContact;
import com.qgyyzs.globalcosmetics.uikit.business.team.helper.TeamHelper;
import com.qgyyzs.globalcosmetics.uikit.business.uinfo.UserInfoHelper;

/**
 * Created by huangjun on 2015/9/8.
 */
public class ContactHelper {
    public static IContact makeContactFromUserInfo(final UserInfo userInfo) {
        return new IContact() {
            @Override
            public String getContactId() {
                return userInfo.getAccount();
            }

            @Override
            public int getContactType() {
                return Type.Friend;
            }

            @Override
            public String getDisplayName() {
                return UserInfoHelper.getUserDisplayName(userInfo.getAccount());
            }
        };
    }

    public static IContact makeContactFromMsgIndexRecord(final MsgIndexRecord record) {
        return new IContact() {
            @Override
            public String getContactId() {
                return record.getSessionId();
            }

            @Override
            public int getContactType() {
                return Type.Msg;
            }

            @Override
            public String getDisplayName() {
                String sessionId = record.getSessionId();
                SessionTypeEnum sessionType = record.getSessionType();

                if (sessionType == SessionTypeEnum.P2P) {
                    return UserInfoHelper.getUserDisplayName(sessionId);
                } else if (sessionType == SessionTypeEnum.Team) {
                    return TeamHelper.getTeamName(sessionId);
                }

                return "";
            }
        };
    }
}
