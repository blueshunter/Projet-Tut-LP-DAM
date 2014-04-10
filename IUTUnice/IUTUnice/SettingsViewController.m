//
//  SettingsViewController.m
//  IUTUnice
//
//  Created by VM Mac on 14/03/2014.
//  Copyright (c) 2014 UNS. All rights reserved.
//

#import "SettingsViewController.h"

@interface SettingsViewController ()

@end

@implementation SettingsViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        self.title =@"Paramêtres";
        
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    tabInfosWifi = [[NSMutableArray alloc] init];
    
    tabSettings= [[NSMutableArray alloc] init];
    [tabSettings addObject:@"Connexion"];
    [tabSettings addObject:@"Infos WIfi"];
    [tabSettings addObject:@"A propos"];
    [tabSettings addObject:@"Sondage"];
    [tabSettings addObject:@"Langue"];
    [tabSettings addObject:@"Credits"];
    
    self.tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    
    self.title=@"Settings";
    
    NSError *error;
    TBXML * tbxml = [TBXML tbxmlWithXMLFile:@"infosWifi" fileExtension:@"xml" error:&error];
    
    if (error) {
        NSLog(@"erreur : %@ %@", [error localizedDescription], [error userInfo]);
    } else {
        NSLog(@"success : %@", [TBXML elementName:tbxml.rootXMLElement]);
        
        //
        if (tbxml.rootXMLElement)
            [self traverseElement:tbxml.rootXMLElement];
    }
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    int number = tabSettings.count;
    
    
    return number;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    tableView.scrollEnabled = NO;
    
    NSString *CellIdentifier = @"Cell1";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == Nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    NSLog(@"%@",[tabSettings objectAtIndex:indexPath.row]);
    
    cell.textLabel.text =[tabSettings objectAtIndex:indexPath.row];
    
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSLog(@"row:%d",[[tableView indexPathForSelectedRow] row]);
    [self choixMenu:[[tableView indexPathForSelectedRow] row]];
    
}



-(void) choixMenu:(int) tag
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    switch(tag)
    {
        case 0: // Connexion
        {
            ConnexionViewController *viewController = [[ConnexionViewController alloc] initWithNibName:@"ConnexionViewController" bundle:nil];
            [self.navigationController pushViewController:viewController animated:YES];
            
            
        }
            break;
        case 1: // Infos Wifi
        {
            infosWifiTableViewController *infosViewController = [[infosWifiTableViewController alloc] initWithNibName:@"infosWifiTableViewController" bundle:nil andInfos:tabInfosWifi];
            
            NSLog(@"count tab : %d" , tabInfosWifi.count);
            
            
            
            [self.navigationController performSelectorOnMainThread:@selector(pushViewController:animated:) withObject:infosViewController waitUntilDone:NO];
            
        }
            break;
        case 2: // A propos
        {
            AboutViewController *viewController = [[AboutViewController alloc] initWithNibName:@"AboutViewController" bundle:nil];
            [self.navigationController pushViewController:viewController animated:YES];
            
        }
            break;
        case 3: // Sondage
        {
            SondageViewController *viewController = [[SondageViewController alloc] initWithNibName:@"SondageViewController" bundle:nil];
            viewController.typeSondage = @"feedback";
            [self.navigationController performSelectorOnMainThread:@selector(pushViewController:animated:) withObject:viewController waitUntilDone:NO];
            
        }
            break;
        case 4: // Langue
        {
            LangueViewController *viewController = [[LangueViewController alloc] initWithNibName:@"LangueViewController" bundle:nil];
            [self.navigationController pushViewController:viewController animated:YES];
            
            
        }
            break;
        case 5: // Crédit
        {
            
            CreditsViewController *viewController = [[CreditsViewController alloc] initWithNibName:@"CreditsViewController" bundle:nil];
            [self.navigationController pushViewController:viewController animated:YES];
            
        }
            
            break;
        default:
            // lancer une exception ? ..
            break;
            
    }
    
}


- (void) traverseElement:(TBXMLElement *)element {
    
    
    
    do {
        if (element->firstChild)
            [self traverseElement:element->firstChild];
        
        if ([[TBXML elementName:element] isEqualToString:@"reseau"]) {
            infosWifi * wifiInfos = [[infosWifi alloc] init];
            //réseau
            
            
            TBXMLElement *elementChild = element->firstChild;
            //nom
            
            for (int i=0; i<5; i++) {
                NSString* str = [NSString stringWithUTF8String:elementChild->text];
                
                if ([[NSString stringWithUTF8String:elementChild->name] isEqual:@"nom"]) {
                    wifiInfos.nom_Wifi =str;
                }else
                if ([[NSString stringWithUTF8String:elementChild->name] isEqual:@"ssid"]) {
                    wifiInfos.ssid_wifi =str;
                }
                else
                if ([[NSString stringWithUTF8String:elementChild->name] isEqual:@"clef"]) {
                    wifiInfos.clef_wifi =str;
                }else
                if ([[NSString stringWithUTF8String:elementChild->name] isEqual:@"login"]) {
                    wifiInfos.login_Wifi =str;
                }else
                if ([[NSString stringWithUTF8String:elementChild->name] isEqual:@"mdp"]) {
                    wifiInfos.mdp_wifi =str;
                }
                NSLog(@"nom : %@", str);
                elementChild = elementChild -> nextSibling;
                
            }
            
    
            
                [tabInfosWifi addObject:wifiInfos];
            
            
            
            NSLog(@"tab infos wifi : %@" , tabInfosWifi);
            NSLog(@"count tab : %d" , tabInfosWifi.count);
        }
        
        
        
        
        
        
    } while ((element = element->nextSibling));
    
    
    
    
    
    
}

@end