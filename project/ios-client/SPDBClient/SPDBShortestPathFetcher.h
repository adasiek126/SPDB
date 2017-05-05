#import <Foundation/Foundation.h>
#import "SPDBMapEntry.h"
#import "SPDBObjectManagerFactory.h"
#import "SPDBShortestPathFetcherDelegate.h"

@interface SPDBShortestPathFetcher : NSObject

@property (nonatomic, strong) id<SPDBShortestPathFetcherDelegate> delegate;

@property (nonatomic, strong) SPDBObjectManagerFactory *objectManagerFactory;
@property (nonatomic, strong) SPDBMapEntry *entryFrom;
@property (nonatomic, strong) SPDBMapEntry *entryTo;

- (void)fetchShortestPathFromEntry:(SPDBMapEntry *)mapEntry toEntry:(SPDBMapEntry *)mapEntry isPublicTransport:(BOOL)publicTransport withChangeTime:(NSNumber *)changeTime delegate:(id<SPDBShortestPathFetcherDelegate>)delegate;

@end
