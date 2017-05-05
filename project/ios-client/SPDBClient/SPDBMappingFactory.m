#import "SPDBMapEntry.h"
#import "SPDBMappingFactory.h"
#import "SPDBRoute.h"

@implementation SPDBMappingFactory

- (RKObjectMapping *)createObjectMappingForMapEntry
{
    RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[SPDBMapEntry class]];
    [mapping addAttributeMappingsFromDictionary:@{
        @"id":                      @"id",
        @"latitude":                @"latitude",
        @"longitude":               @"longitude"
    }];
    
    return mapping;
}

- (RKObjectMapping *)createObjectMappingForRoute
{
    RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[SPDBRoute class]];
    
    [mapping addAttributeMappingsFromDictionary:@{@"id": @"id"}];
    [mapping addPropertyMapping:[RKRelationshipMapping  relationshipMappingFromKeyPath:@"routeFrom"
                                                        toKeyPath:@"routeFrom"
                                                        withMapping:[self createObjectMappingForMapEntry]]];
    [mapping addPropertyMapping:[RKRelationshipMapping  relationshipMappingFromKeyPath:@"routeTo"
                                                        toKeyPath:@"routeTo"
                                                        withMapping:[self createObjectMappingForMapEntry]]];
    [mapping addAttributeMappingsFromDictionary:@{@"duration": @"duration"}];
    [mapping addAttributeMappingsFromDictionary:@{@"line": @"line"}];
        
    return mapping;
}

@end
