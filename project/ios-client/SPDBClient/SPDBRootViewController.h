#import <MBProgressHUD/MBProgressHUD.h>
#import "SPDBMapSelectionDelegate.h"
#import "SPDBShortestPathFetcher.h"
#import <UIKit/UIKit.h>

@interface SPDBRootViewController : UIViewController<UITableViewDataSource, UITableViewDelegate, SPDBMapSelectionDelegate, SPDBShortestPathFetcherDelegate>

@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) MBProgressHUD *progressHud;

@property (nonatomic, strong) NSValue *pointFrom;
@property (nonatomic, strong) NSValue *pointTo;
@property (nonatomic, strong) NSValue *selectedPoint;
@property (nonatomic, strong) NSArray *foundRoute;
@property (nonatomic, strong) NSDate *selectedArrivalTime;

@end
