package com.qgyyzs.globalcosmetics.uikit.impl.customization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.team.model.Team;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.uikit.api.NimUIKit;
import com.qgyyzs.globalcosmetics.uikit.api.model.session.SessionCustomization;
import com.qgyyzs.globalcosmetics.uikit.business.team.model.TeamExtras;
import com.qgyyzs.globalcosmetics.uikit.business.team.model.TeamRequestCode;

import java.util.ArrayList;

/**
 * SessionCustomization 可以实现聊天界面定制项：
 * 1. 聊天背景 <br>
 * 2. 加号展开后的按钮和动作，如自定义消息 <br>
 * 3. ActionBar右侧按钮。
 * <p>
 * DefaultTeamSessionCustomization 提供默认的群聊界面定制，添加了ActionBar右侧按钮，用于跳转群信息界面
 * <p>
 * Created by hzchenkang on 2016/12/20.
 */

public class DefaultTeamSessionCustomization extends SessionCustomization {

    public DefaultTeamSessionCustomization() {

        // ActionBar右侧按钮，跳转至群信息界面
        SessionCustomization.OptionsButton infoButton = new SessionCustomization.OptionsButton() {
            @Override
            public void onClick(Context context, View view, String sessionId) {
                Team team = NimUIKit.getTeamProvider().getTeamById(sessionId);
                if (team != null && team.isMyTeam()) {
                    NimUIKit.startTeamInfo(context, sessionId);
                } else {
                    Toast.makeText(context, R.string.team_invalid_tip, Toast.LENGTH_SHORT).show();
                }
            }
        };
        infoButton.iconId = R.drawable.nim_ic_message_actionbar_team;
        buttons = new ArrayList<>();
        buttons.add(infoButton);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == TeamRequestCode.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String reason = data.getStringExtra(TeamExtras.RESULT_EXTRA_REASON);
                boolean finish = reason != null && (reason.equals(TeamExtras
                        .RESULT_EXTRA_REASON_DISMISS) || reason.equals(TeamExtras.RESULT_EXTRA_REASON_QUIT));
                if (finish) {
                    activity.finish(); // 退出or解散群直接退出多人会话
                }
            }
        }
    }

}
