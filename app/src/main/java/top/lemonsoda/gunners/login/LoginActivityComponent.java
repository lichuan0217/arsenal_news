package top.lemonsoda.gunners.login;

import dagger.Component;
import top.lemonsoda.gunners.NewsApplicationComponent;
import top.lemonsoda.gunners.utils.di.LoginActivityScope;

/**
 * Created by Chuan on 03/03/2017.
 */

@LoginActivityScope
@Component(dependencies = NewsApplicationComponent.class)
public interface LoginActivityComponent {

    void inject(LoginActivity activity);
}
