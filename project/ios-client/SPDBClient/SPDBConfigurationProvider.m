#import "SPDBConfigurationProvider.h"

@implementation SPDBConfigurationProvider

+ (NSDictionary *)configurationProperties
{
    NSString *configPlist = [[NSBundle mainBundle] pathForResource:@"config" ofType:@"plist"];
    return [[NSDictionary alloc] initWithContentsOfFile:configPlist];
}

+ (NSString *)getServiceAddress
{
    return [[NSUserDefaults standardUserDefaults] valueForKey:@"serviceAddressPreference"];
}

@end
