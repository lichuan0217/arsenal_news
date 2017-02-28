# Knowledge Share


## ViewPager

_**Question**_:
How to distinguish **programmatically-initiated smooth scroll** and a **user-initiated touch scroll**

Use `onPageScrollStateChanged(int state)` in `ViewPager.onPageChangeListener`

Essentially there are **three states** that a page in a ViewPager can be in:
- **Dragging**: Indicates that the pager is currently being dragged by the user.
- **Idle**: Indicates that the pager is in an idle, settled state.
- **Settling**: Indicates that the pager is in the process of settling to a final position.

When **user-initiated touch scroll**, the state change will be:
`Dragging --> Setting --> Idle`

When **programmatically-initiated smooth scroll**, the state change will be:
`Setting --> Idle`

Example codes:
```
    public void onPageScrollStateChanged (int state)
        {
            if (previousState == ViewPager.SCROLL_STATE_DRAGGING
                    && state == ViewPager.SCROLL_STATE_SETTLING)
                userScrollChange = true;

            else if (previousState == ViewPager.SCROLL_STATE_SETTLING
                    && state == ViewPager.SCROLL_STATE_IDLE)
                userScrollChange = false;

            previousState = state;
        }`
```

Reference: [differentiating-between-user-scroll-and-programatic-page-change-in-viewpager](http://stackoverflow.com/questions/17819970/differentiating-between-user-scroll-and-programatic-page-change-in-viewpager)