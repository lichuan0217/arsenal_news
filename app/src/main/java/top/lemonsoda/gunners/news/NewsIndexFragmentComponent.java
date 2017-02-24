package top.lemonsoda.gunners.news;

import dagger.Component;
import top.lemonsoda.gunners.utils.di.NewsIndexFragmentScope;

/**
 * Created by chuanl on 2/24/17.
 */

@NewsIndexFragmentScope
@Component(modules = NewsIndexFragmentModule.class)
public interface NewsIndexFragmentComponent {

    void inject(NewsIndexFragment fragment);
}