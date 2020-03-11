package com.qgyyzs.globalcosmetics.uikit.business.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseModel;
import com.qgyyzs.globalcosmetics.mvp.model.ProductBean;
import com.qgyyzs.globalcosmetics.mvp.presenter.TeamProductPresenter;
import com.qgyyzs.globalcosmetics.mvp.view.ProductListView;
import com.qgyyzs.globalcosmetics.ui.customview.TeamAdView;
import com.qgyyzs.globalcosmetics.uikit.api.NimUIKit;
import com.qgyyzs.globalcosmetics.uikit.api.model.SimpleCallback;
import com.qgyyzs.globalcosmetics.uikit.api.model.contact.ContactChangedObserver;
import com.qgyyzs.globalcosmetics.uikit.api.model.session.SessionCustomization;
import com.qgyyzs.globalcosmetics.uikit.api.model.team.TeamDataChangedObserver;
import com.qgyyzs.globalcosmetics.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.qgyyzs.globalcosmetics.uikit.api.wrapper.NimToolBarOptions;
import com.qgyyzs.globalcosmetics.uikit.business.session.constant.Extras;
import com.qgyyzs.globalcosmetics.uikit.business.session.fragment.MessageFragment;
import com.qgyyzs.globalcosmetics.uikit.business.session.fragment.TeamMessageFragment;
import com.qgyyzs.globalcosmetics.uikit.business.team.activity.AdvancedTeamInfoActivity;
import com.qgyyzs.globalcosmetics.uikit.common.activity.ToolBarOptions;

import java.util.List;

/**
 * 群聊界面
 * <p/>
 * Created by huangjun on 2015/3/5.
 */
public class TeamMessageActivity extends BaseMessageActivity implements ProductListView {
    private RelativeLayout titlebar;
    private ImageView mIvback, mIvRight;
    private TextView mTvTitle;
    private TeamAdView teamAdView;
    private TeamProductPresenter presenter = new TeamProductPresenter(this);

    // model
    private Team team;

    private View invalidTeamTipView;

    private TextView invalidTeamTipText;

    private TeamMessageFragment fragment;

    private Class<? extends Activity> backToClass;

    public static void start(Context context, String tid, SessionCustomization customization,
                             Class<? extends Activity> backToClass, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, tid);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(Extras.EXTRA_BACK_TO_CLASS, backToClass);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, TeamMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    protected void findViews() {
        invalidTeamTipView = findView(R.id.invalid_team_tip);
        invalidTeamTipText = findView(R.id.invalid_team_text);
        mIvback = findView(R.id.back);
        mTvTitle = findView(R.id.title_text);
        mIvRight = findView(R.id.right_img);
        titlebar = findView(R.id.titlebar);
        teamAdView = findView(R.id.teamView);
        mIvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamInfoActivity.start(TeamMessageActivity.this, team.getId());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backToClass = (Class<? extends Activity>) getIntent().getSerializableExtra(Extras.EXTRA_BACK_TO_CLASS);
        findViews();
//        StatusBarUtil.immersive(this);

        registerTeamUpdateObserver(true);
        presenter.getProduct(sessionId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }

        registerTeamUpdateObserver(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestTeamInfo();
    }

    /**
     * 请求群基本信息
     */
    private void requestTeamInfo() {
//        LogUtils.e("sessionId"+sessionId);
        // 请求群基本信息
        Team t = NimUIKit.getTeamProvider().getTeamById(sessionId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            NimUIKit.getTeamProvider().fetchTeamById(sessionId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onRequestTeamInfoFailed();
                    }
                }
            });
        }
    }

    private void onRequestTeamInfoFailed() {
        Toast.makeText(TeamMessageActivity.this, "获取群组信息失败!", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 更新群信息
     *
     * @param d
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }

        team = d;
        fragment.setTeam(team);

        setTitle(team == null ? sessionId : team.getName() + "(" + team.getMemberCount() + "人)");
        mTvTitle.setText(team == null ? sessionId : team.getName() + "(" + team.getMemberCount() + "人)");

        invalidTeamTipText.setText(team.getType() == TeamTypeEnum.Normal ? R.string.normal_team_invalid_tip : R.string.team_invalid_tip);
        invalidTeamTipView.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
    }

    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team == null) {
                return;
            }
            if (team.getId().equals(TeamMessageActivity.this.team.getId())) {
                updateTeamInfo(team);
            }
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {
        }
    };

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            fragment.refreshMessageList();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            fragment.refreshMessageList();
        }
    };

    @Override
    protected MessageFragment fragment() {
        // 添加fragment
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.Team);
        fragment = new TeamMessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_team_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "群聊";
        setToolBar(R.id.toolbar, options);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (backToClass != null) {
            Intent intent = new Intent();
            intent.setClass(this, backToClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void onErrorCode(BaseModel model) {

    }

    @Override
    public void showProductListResult(List<ProductBean> list, int page) {
        if (TeamMessageActivity.this == null) return;
        if (list == null) return;
        if (teamAdView != null) teamAdView.setViews(this, list);
        if (teamAdView != null) teamAdView.startFlipping();
    }

    @Override
    public void showLoadingLayout() {

    }

    @Override
    public void showSuccessLayout() {

    }

    @Override
    public void showErrorLayout() {

    }

    @Override
    public boolean isVisable() {
        return false;
    }
}
