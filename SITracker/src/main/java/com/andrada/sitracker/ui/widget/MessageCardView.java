package com.andrada.sitracker.ui.widget;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.andrada.sitracker.R;
import com.andrada.sitracker.util.UIUtils;

import static com.andrada.sitracker.util.LogUtils.LOGW;
import static com.andrada.sitracker.util.LogUtils.makeLogTag;

public class MessageCardView extends FrameLayout implements View.OnClickListener {

    public static final int ANIM_DURATION = 300;
    private static final String TAG = makeLogTag("MessageCardView");
    private TextView mTitleView;
    private TextView mMessageView;
    private Button[] mButtons;
    private String[] mButtonTags;
    private OnMessageCardButtonClicked mListener = null;
    private View mRoot;

    public MessageCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageCardView(Context context) {
        this(context, null, 0);
    }

    public MessageCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflater.inflate(R.layout.message_card, this, true);
        mTitleView = (TextView) mRoot.findViewById(R.id.title);
        mMessageView = (TextView) mRoot.findViewById(R.id.text);
        mButtons = new Button[]{
                (Button) mRoot.findViewById(R.id.button1),
                (Button) mRoot.findViewById(R.id.button2)
        };
        mButtonTags = new String[]{"", ""};

        for (Button button : mButtons) {
            button.setVisibility(View.GONE);
            button.setOnClickListener(this);
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MessageCard, 0, 0);
        String title = a.getString(R.styleable.MessageCard_messageTitle);
        setTitle(title);
        String text = a.getString(R.styleable.MessageCard_messageText);
        if (text != null) {
            setText(text);
        }
        String button1text = a.getString(R.styleable.MessageCard_button1text);
        boolean button1emphasis = a.getBoolean(R.styleable.MessageCard_button1emphasis, false);
        String button1tag = a.getString(R.styleable.MessageCard_button1tag);
        String button2text = a.getString(R.styleable.MessageCard_button2text);
        boolean button2emphasis = a.getBoolean(R.styleable.MessageCard_button2emphasis, false);
        String button2tag = a.getString(R.styleable.MessageCard_button2tag);
        int emphasisColor = a.getColor(R.styleable.MessageCard_emphasisColor,
                getResources().getColor(R.color.theme_primary));

        if (button1text != null) {
            setButton(0, button1text, button1tag, button1emphasis, 0);
        }
        if (button2text != null) {
            setButton(1, button2text, button2tag, button2emphasis, emphasisColor);
        }
    }

    public void setListener(OnMessageCardButtonClicked listener) {
        mListener = listener;
    }

    public void setButton(int index, String text, String tag, boolean emphasis, int emphasisColor) {
        if (index < 0 || index >= mButtons.length) {
            LOGW(TAG, "Invalid button index: " + index);
            return;
        }
        mButtons[index].setText(text);
        mButtons[index].setVisibility(View.VISIBLE);
        mButtonTags[index] = tag;
        if (emphasis) {
            if (emphasisColor == 0) {
                emphasisColor = getResources().getColor(R.color.theme_primary);
            }
            mButtons[index].setTextColor(emphasisColor);
            mButtons[index].setTypeface(null, Typeface.BOLD);
        }
    }

    /**
     * Use sparingly.
     */
    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            mTitleView.setVisibility(View.GONE);
        } else {
            mTitleView.setVisibility(View.VISIBLE);
            mTitleView.setText(title);
        }
    }

    public void setText(String text) {
        mMessageView.setText(text);
    }

    public void overrideBackground(int bgResId) {
        findViewById(R.id.card_root).setBackgroundResource(bgResId);
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }

        for (int i = 0; i < mButtons.length; i++) {
            if (mButtons[i] == v) {
                mListener.onMessageCardButtonClicked(mButtonTags[i]);
                break;
            }
        }
    }

    public void dismiss() {
        dismiss(false);
    }

    @SuppressLint("NewApi")
    public void dismiss(boolean animate) {
        if (!animate) {
            setVisibility(View.GONE);
        } else {
            if (UIUtils.hasICS()) {
                animate()
                        .scaleY(0.1f)
                        .alpha(0.1f)
                        .setDuration(ANIM_DURATION)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) { }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
            } else {
                //TODO get rid of this on next release with API 14
                com.nineoldandroids.view.ViewPropertyAnimator.animate(this)
                        .scaleY(0.1f)
                        .alpha(0.1f)
                        .setDuration(ANIM_DURATION)
                        .setListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                                setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

                            }
                        });
            }

        }
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public interface OnMessageCardButtonClicked {

        public void onMessageCardButtonClicked(String tag);
    }
}
