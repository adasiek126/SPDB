#import "SPDBAppDelegate.h"

@implementation SPDBAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSDictionary *appDefaults = [NSDictionary dictionaryWithObjectsAndKeys:
                                    @"300", @"changeTimePreference",
                                    @"http://localhost:8080/spdb-web-services-1.0/", @"serviceAddressPreference",
                                    nil
                                 ];
    
    [defaults registerDefaults:appDefaults];
    [defaults synchronize];
    
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
}

- (void)applicationWillTerminate:(UIApplication *)application
{
}

@end
